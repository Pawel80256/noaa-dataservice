import {useTranslation} from "react-i18next";
import {Button, Col, Flex, notification, Row, Space, Typography} from "antd";
import {useState} from "react";
import {NOAADataType} from "../models/NOAADataType";
import {NOAALocation} from "../models/NOAALocation";
import {
    deleteLocalDataTypesByIds,
    getAllLocalDataTypes,
    getAllRemoteDataTypes,
    loadAllDataTypes,
    loadDataTypesByIds
} from "../services/NOAADataTypeService";
import {showErrorNotification, showSuccessNotification, showWarningNotification} from "../services/Utils";
import {
    deleteCountriesByIds,
    getAllLocalCountries,
    getAllRemoteCountries, loadAllCountries,
    loadCountriesByIds
} from "../services/NOAALocationService";
import {DataTypesTable} from "../components/data_loader/data_types/DataTypesTable";
import {DownloadOutlined} from "@ant-design/icons";

export const CountriesLoaderView = () => {
    const {t} = useTranslation();
    const [api, contextHolder] = notification.useNotification();

    const [remoteCountries, setRemoteCountries] = useState<NOAALocation[]>([]);
    const [localCountries, setLocalCountries] = useState<NOAALocation[]>([]);

    const [isRemoteCountriesLoading, setIsRemoteCountriesLoading] = useState(false);
    const [isLocalCountriesLoading, setIsLocalCountriesLoading] = useState(false)
    const [isLoadingSelectedCountriesLoading, setIsLoadingSelectedCountriesLoading] = useState(false)
    const [isLoadingAllCountriesLoading, setIsLoadingAllCountriesLoading] = useState(false)
    const [isDeletingCountriesLoading, setIsDeletingCountriesLoading] = useState(false)

    const [selectedRemoteCountries, setSelectedRemoteCountries] = useState<React.Key[]>([]);
    const [selectedLocalCountries, setSelectedLocalCountries] = useState<React.Key[]>([]);

    const updateSelectedRemoteCountries = (keys: React.Key[]) => {
        setSelectedRemoteCountries(keys)
    }
    const updateSelectedLocalCountries = (keys: React.Key[]) => {
        setSelectedLocalCountries(keys)
    }

    const selectAllLocalDataTypes = () => {
        const newSelectedRowKeys = localCountries.map(dt => dt.id);
        setSelectedLocalCountries(newSelectedRowKeys);
    };

    const selectAllRemoteDataTypes = () => {
        const newSelectedRowKeys = remoteCountries.map(dt => dt.id);
        setSelectedRemoteCountries(newSelectedRowKeys);
    }

    const fetchRemoteDataTypes = () => {
        setIsRemoteCountriesLoading(true);
        getAllRemoteCountries().then(response => {
            setRemoteCountries(response);
            setIsRemoteCountriesLoading(false);
            showSuccessNotification(t('REMOTE_FETCH_SUCCESS_LABEL'));
        }).catch(() => {
            setIsRemoteCountriesLoading(false);
            showErrorNotification(t('REMOTE_FETCH_ERROR_LABEL'));
        });
    };

    const fetchLocalCountries = () => {
        setIsLocalCountriesLoading(true);
        getAllLocalCountries().then(response => {
            setLocalCountries(response);
            setIsLocalCountriesLoading(false);
            showSuccessNotification(t('LOCAL_FETCH_SUCCESS_LABEL'))
        }).catch(()=>{
            setIsLocalCountriesLoading(false);
            showErrorNotification(t('LOCAL_FETCH_ERROR_LABEL'))
        })
    }

    const handleLoadingSelectedCountries = () => {
        const ids:string[] = selectedRemoteCountries.map(key => key.toString());

        if (ids.length > 60) {
            showWarningNotification(t('LOAD_LIMIT_EXCEEDED_LABEL'))
            return;
        }

        setIsLoadingSelectedCountriesLoading(true);
        loadCountriesByIds(ids).then(() => {
            fetchLocalCountries();
            setIsLoadingSelectedCountriesLoading(false);
            showSuccessNotification(t('LOAD_SUCCESS_LABEL'))
        }).catch(()=>{
            setIsLoadingSelectedCountriesLoading(false);
            showErrorNotification(t('LOAD_ERROR_LABEL'))
        })
    }

    const handleLoadingAllCountries = () =>{
        setIsLoadingAllCountriesLoading(true);
        loadAllCountries().then(()=>{
            fetchLocalCountries();
            setIsLoadingAllCountriesLoading(false);
            showSuccessNotification(t('LOAD_SUCCESS_LABEL'))
        }).catch(()=>{
            setIsLoadingAllCountriesLoading(false);
            showErrorNotification(t('LOAD_ERROR_LABEL'))
        })
    }

    const deleteSelectedCountries = () => {
        const ids:string[] = selectedLocalCountries.map(key => key.toString());
        setIsDeletingCountriesLoading(true);
        deleteCountriesByIds(ids).then(() => {
            fetchLocalCountries(/*boolean showNotification*/);
            setIsDeletingCountriesLoading(false);
            showSuccessNotification(t('DELETE_SUCCESS_LABEL'))
        }).catch(()=>{
            setIsDeletingCountriesLoading(false);
            showErrorNotification(t('DELETE_ERROR_LABEL'))
        })
    }

    return(
        <>
            {contextHolder}
            <div style={{ display: 'flex', flexDirection: 'column', height: '100vh' }}>
                <Flex style={{ flex: 1, minWidth: '50vh' }} align={'center'} justify={'flex-start'} vertical>
                    <Row>
                        <Typography.Title level={2}>{t('REMOTE_CONTENT')}</Typography.Title>
                    </Row>
                    <Row>
                        <DataTypesTable
                            dataTypes={remoteCountries}
                            updateSelectedDataTypes={updateSelectedRemoteCountries}
                            localDataTypes={localCountries}
                            selectedDataTypes={selectedRemoteCountries}
                            showStatusColumn={true}/>
                    </Row>
                    {remoteCountries.length === 0 &&    <Row>
                        <Button
                            style={{marginTop:'25px'}}
                            type="primary" icon={<DownloadOutlined />}
                            onClick={fetchRemoteDataTypes}
                            loading={isRemoteCountriesLoading}>{t('FETCH_REMOTE_LABEL')}</Button>
                    </Row>}
                    {remoteCountries.length > 0 &&    <Row>
                        <Col>
                            <Space>
                                <Button
                                    style={{marginTop:'25px'}}
                                    type="primary" icon={<DownloadOutlined />}
                                    onClick={handleLoadingAllCountries}
                                    loading={isLoadingAllCountriesLoading}>{t('LOAD_ALL_LABEL')}</Button>
                                <Button
                                    style={{marginTop:'25px'}}
                                    type="primary" icon={<DownloadOutlined />}
                                    onClick={handleLoadingSelectedCountries}
                                    loading={isLoadingSelectedCountriesLoading}>{t('LOAD_SELECTED_LABEL')}</Button>
                            </Space>
                        </Col>
                    </Row>}
                </Flex>
                <Flex style={{ flex: 1, minHeight: '50vh' }} align={'center'} justify={'flex-start'} vertical>
                    <Row>
                        <Typography.Title level={2}>{t('DATABASE_CONTENT')}</Typography.Title>
                    </Row>
                    <Row>
                        <DataTypesTable
                            dataTypes={localCountries}
                            updateSelectedDataTypes={updateSelectedLocalCountries}
                            localDataTypes={[]}
                            selectedDataTypes={selectedLocalCountries}
                            showStatusColumn={false}/>
                    </Row>
                    {localCountries.length === 0 &&    <Row>
                        <Button
                            style={{marginTop:'25px'}}
                            type="primary" icon={<DownloadOutlined />}
                            onClick={fetchLocalCountries}
                            loading={isLocalCountriesLoading}>{t('FETCH_LOCAL_LABEL')}</Button>
                    </Row>}
                    {localCountries.length > 0 &&    <Row>
                        <Col>
                            <Space>
                                <Button
                                    style={{marginTop:'25px'}}
                                    type="primary" icon={<DownloadOutlined />}
                                    onClick={selectAllLocalDataTypes}
                                    loading={isDeletingCountriesLoading}>{t('SELECT_ALL_LABEL')}</Button>
                                <Button
                                    style={{marginTop:'25px'}}
                                    type="primary" icon={<DownloadOutlined />}
                                    onClick={deleteSelectedCountries}
                                    loading={isDeletingCountriesLoading}>{t('DELETE_SELECTED_LABEL')}</Button>
                            </Space>
                        </Col>
                    </Row>}
                </Flex>
            </div>

        </>
    )
}
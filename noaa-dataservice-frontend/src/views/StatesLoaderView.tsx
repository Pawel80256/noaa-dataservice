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
    deleteLocationsByIds,
    getAllLocalStates,
    getAllRemoteStates, loadAllCountries, loadAllStates,
    loadStatesByIds
} from "../services/NOAALocationService";
import {DataTypesTable} from "../components/data_loader/data_types/DataTypesTable";
import {DownloadOutlined} from "@ant-design/icons";

export const StatesLoaderView = () => {
    const {t} = useTranslation();
    const [api, contextHolder] = notification.useNotification();

    const [remoteStates, setRemoteStates] = useState<NOAALocation[]>([]);
    const [localStates, setLocalStates] = useState<NOAALocation[]>([]);

    const [isRemoteStatesLoading, setIsRemoteStatesLoading] = useState(false);
    const [isLocalStatesLoading, setIsLocalStatesLoading] = useState(false)
    const [isLoadingSelectedStatesLoading, setIsLoadingSelectedStatesLoading] = useState(false)
    const [isLoadingAllStatesLoading, setIsLoadingAllStatesLoading] = useState(false)
    const [isDeletingStatesLoading, setIsDeletingStatesLoading] = useState(false)

    const [selectedRemoteStates, setSelectedRemoteStates] = useState<React.Key[]>([]);
    const [selectedLocalStates, setSelectedLocalStates] = useState<React.Key[]>([]);

    const updateSelectedRemoteStates = (keys: React.Key[]) => {
        setSelectedRemoteStates(keys)
    }
    const updateSelectedLocalStates = (keys: React.Key[]) => {
        setSelectedLocalStates(keys)
    }

    const selectAllLocal = () => {
        if (selectedLocalStates.length === localStates.length) {
            setSelectedLocalStates([]);
        } else {
            const newSelectedRowKeys = localStates.map(dt => dt.id);
            setSelectedLocalStates(newSelectedRowKeys);
        }
    };

    const selectAllRemote = () => {
        if (selectedRemoteStates.length === remoteStates.length) {
            setSelectedRemoteStates([]);
        } else {
            const newSelectedRowKeys = remoteStates.map(dt => dt.id);
            setSelectedRemoteStates(newSelectedRowKeys);
        }
    };

    const fetchRemoteStates = () => {
        setIsRemoteStatesLoading(true);
        getAllRemoteStates().then(response => {
            setRemoteStates(response);
            setIsRemoteStatesLoading(false);
            showSuccessNotification(t('REMOTE_FETCH_SUCCESS_LABEL'));
        }).catch(() => {
            setIsRemoteStatesLoading(false);
            showErrorNotification(t('REMOTE_FETCH_ERROR_LABEL'));
        });
    };

    const fetchLocalStates = () => {
        setIsLocalStatesLoading(true);
        getAllLocalStates().then(response => {
            setLocalStates(response);
            setIsLocalStatesLoading(false);
            showSuccessNotification(t('LOCAL_FETCH_SUCCESS_LABEL'))
        }).catch(()=>{
            setIsLocalStatesLoading(false);
            showErrorNotification(t('LOCAL_FETCH_ERROR_LABEL'))
        })
    }

    const handleLoadingSelectedStates = () => {
        const ids:string[] = selectedRemoteStates.map(key => key.toString());

        if (ids.length > 60) {
            showWarningNotification(t('LOAD_LIMIT_EXCEEDED_LABEL'))
            return;
        }

        setIsLoadingSelectedStatesLoading(true);
        loadStatesByIds(ids).then(() => {
            fetchLocalStates();
            setIsLoadingSelectedStatesLoading(false);
            showSuccessNotification(t('LOAD_SUCCESS_LABEL'))
        }).catch(()=>{
            setIsLoadingSelectedStatesLoading(false);
            showErrorNotification(t('LOAD_ERROR_LABEL'))
        })
    }

    const handleLoadingAllStates = () =>{
        setIsLoadingAllStatesLoading(true);
        loadAllStates().then(()=>{
            fetchLocalStates();
            setIsLoadingAllStatesLoading(false);
            showSuccessNotification(t('LOAD_SUCCESS_LABEL'))
        }).catch(()=>{
            setIsLoadingAllStatesLoading(false);
            showErrorNotification(t('LOAD_ERROR_LABEL'))
        })
    }

    const deleteSelectedStates = () => {
        const ids:string[] = selectedLocalStates.map(key => key.toString());
        setIsDeletingStatesLoading(true);
        deleteLocationsByIds(ids).then(() => {
            const updatedSelectedStates = selectedLocalStates.filter(key => !ids.includes(key.toString()));
            setSelectedLocalStates(updatedSelectedStates);

            fetchLocalStates(/*boolean showNotification*/);
            setIsDeletingStatesLoading(false);
            showSuccessNotification(t('DELETE_SUCCESS_LABEL'))
        }).catch(()=>{
            setIsDeletingStatesLoading(false);
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
                            dataTypes={remoteStates}
                            updateSelectedDataTypes={updateSelectedRemoteStates}
                            localDataTypes={localStates}
                            selectedDataTypes={selectedRemoteStates}
                            showStatusColumn={true}/>
                    </Row>
                    {remoteStates.length === 0 &&    <Row>
                        <Button
                            style={{marginTop:'25px'}}
                            type="primary" icon={<DownloadOutlined />}
                            onClick={fetchRemoteStates}
                            loading={isRemoteStatesLoading}>{t('FETCH_REMOTE_LABEL')}</Button>
                    </Row>}
                    {remoteStates.length > 0 &&    <Row>
                        <Col>
                            <Space>
                                <Button
                                    style={{marginTop:'25px'}}
                                    type="primary" icon={<DownloadOutlined />}
                                    onClick={selectAllRemote}
                                    loading={isLoadingAllStatesLoading}>{t('SELECT_ALL_LABEL')}</Button>
                                <Button
                                    style={{marginTop:'25px'}}
                                    type="primary" icon={<DownloadOutlined />}
                                    onClick={handleLoadingSelectedStates}
                                    loading={isLoadingSelectedStatesLoading}>{t('LOAD_SELECTED_LABEL')}</Button>
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
                            dataTypes={localStates}
                            updateSelectedDataTypes={updateSelectedLocalStates}
                            localDataTypes={[]}
                            selectedDataTypes={selectedLocalStates}
                            showStatusColumn={false}/>
                    </Row>
                    {localStates.length === 0 &&    <Row>
                        <Button
                            style={{marginTop:'25px'}}
                            type="primary" icon={<DownloadOutlined />}
                            onClick={fetchLocalStates}
                            loading={isLocalStatesLoading}>{t('FETCH_LOCAL_LABEL')}</Button>
                    </Row>}
                    {localStates.length > 0 &&    <Row>
                        <Col>
                            <Space>
                                <Button
                                    style={{marginTop:'25px'}}
                                    type="primary" icon={<DownloadOutlined />}
                                    onClick={selectAllLocal}
                                    loading={isDeletingStatesLoading}>{t('SELECT_ALL_LABEL')}</Button>
                                <Button
                                    style={{marginTop:'25px'}}
                                    type="primary" icon={<DownloadOutlined />}
                                    onClick={deleteSelectedStates}
                                    loading={isDeletingStatesLoading}>{t('DELETE_SELECTED_LABEL')}</Button>
                            </Space>
                        </Col>
                    </Row>}
                </Flex>
            </div>

        </>
    )
}

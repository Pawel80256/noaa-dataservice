import {useTranslation} from "react-i18next";
import {Button, Col, Divider, Flex, notification, Row, Space, Typography} from "antd";
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
    getAllLocalCities,
    getAllRemoteCities,
    loadCitiesByIds
} from "../services/NOAALocationService";
import {DataTypesTable} from "../components/data_loader/data_types/DataTypesTable";
import {DownloadOutlined} from "@ant-design/icons";
import {LocationsTable} from "../components/data_loader/locations/LocationsTable";

export const CitiesLoaderView = () => {
    const {t} = useTranslation();
    const [api, contextHolder] = notification.useNotification();

    const [remoteCities, setRemoteCities] = useState<NOAALocation[]>([]);
    const [localCities, setLocalCities] = useState<NOAALocation[]>([]);

    const [isRemoteCitiesLoading, setIsRemoteCitiesLoading] = useState(false);
    const [isLocalCitiesLoading, setIsLocalCitiesLoading] = useState(false)
    const [isLoadingSelectedCitiesLoading, setIsLoadingSelectedCitiesLoading] = useState(false)
    // const [isLoadingAllDataTypesLoading, setIsLoadingAllDataTypesLoading] = useState(false)
    const [isDeletingCitiesLoading, setIsDeletingCitiesLoading] = useState(false)

    const [selectedRemoteCities, setSelectedRemoteCities] = useState<React.Key[]>([]);
    const [selectedLocalCities, setSelectedLocalCities] = useState<React.Key[]>([]);

    const updateSelectedRemoteCities = (keys: React.Key[]) => {
        setSelectedRemoteCities(keys)
    }
    const updateSelectedLocalCities = (keys: React.Key[]) => {
        setSelectedLocalCities(keys)
    }

    const selectAllLocal = () => {
        if (selectedLocalCities.length === localCities.length) {
            setSelectedLocalCities([]);
        } else {
            const newSelectedRowKeys = localCities.map(dt => dt.id);
            setSelectedLocalCities(newSelectedRowKeys);
        }
    };

    const selectAllRemote = () => {
        if (selectedRemoteCities.length === remoteCities.length) {
            setSelectedRemoteCities([]);
        } else {
            const newSelectedRowKeys = remoteCities.map(dt => dt.id);
            setSelectedRemoteCities(newSelectedRowKeys);
        }
    };

    const fetchRemoteDataTypes = () => {
        setIsRemoteCitiesLoading(true);
        getAllRemoteCities().then(response => {
            setRemoteCities(response);
            setIsRemoteCitiesLoading(false);
            showSuccessNotification(t('REMOTE_FETCH_SUCCESS_LABEL'));
        }).catch(() => {
            setIsRemoteCitiesLoading(false);
            showErrorNotification(t('REMOTE_FETCH_ERROR_LABEL'));
        });
    };

    const fetchLocalCities = () => {
        setIsLocalCitiesLoading(true);
        getAllLocalCities().then(response => {
            setLocalCities(response);
            setIsLocalCitiesLoading(false);
            showSuccessNotification(t('LOCAL_FETCH_SUCCESS_LABEL'))
        }).catch(()=>{
            setIsLocalCitiesLoading(false);
            showErrorNotification(t('LOCAL_FETCH_ERROR_LABEL'))
        })
    }

    const handleLoadingSelectedCities = () => {
        const ids:string[] = selectedRemoteCities.map(key => key.toString());

        // if (ids.length > 60) {
        //     showWarningNotification(t('LOAD_LIMIT_EXCEEDED_LABEL'))
        //     return;
        // }

        setIsLoadingSelectedCitiesLoading(true);
        loadCitiesByIds(ids).then(() => {
            fetchLocalCities();
            setIsLoadingSelectedCitiesLoading(false);
            showSuccessNotification(t('LOAD_SUCCESS_LABEL'))
        }).catch(()=>{
            setIsLoadingSelectedCitiesLoading(false);
            showErrorNotification(t('LOAD_ERROR_LABEL'))
        })
    }

    const deleteSelectedCities = () => {
        const ids:string[] = selectedLocalCities.map(key => key.toString());
        setIsDeletingCitiesLoading(true);
        deleteLocationsByIds(ids).then(() => {
            const updatedSelectedCities = selectedLocalCities.filter(key => !ids.includes(key.toString()));
            setSelectedLocalCities(updatedSelectedCities);

            fetchLocalCities(/*boolean showNotification*/);
            setIsDeletingCitiesLoading(false);
            showSuccessNotification(t('DELETE_SUCCESS_LABEL'))
        }).catch(()=>{
            setIsDeletingCitiesLoading(false);
            showErrorNotification(t('DELETE_ERROR_LABEL'))
        })
    }

    return(
        <>
            {contextHolder}
            <div style={{ display: 'flex', flexDirection: 'column', height: '100vh' }}>
                <Flex style={{ flex: 1, minWidth: '50vh' }} align={'center'} justify={'flex-start'} vertical>
                    <Row>
                        <Typography.Title>{t('CITIES')}</Typography.Title>
                    </Row>
                    <Divider />
                    <Row>
                        <Typography.Title level={2}>{t('REMOTE_CONTENT')}</Typography.Title>
                    </Row>
                    <Row>
                        <LocationsTable
                            locations={remoteCities}
                            updateSelectedLocations={updateSelectedRemoteCities}
                            localLocations={localCities}
                            selectedLocations={selectedRemoteCities}
                            showStatusColumn={true}/>
                    </Row>
                    {remoteCities.length === 0 &&    <Row>
                        <Button
                            style={{marginTop:'25px'}}
                            type="primary" icon={<DownloadOutlined />}
                            onClick={fetchRemoteDataTypes}
                            loading={isRemoteCitiesLoading}>{t('FETCH_REMOTE_LABEL')}</Button>
                    </Row>}
                    {remoteCities.length > 0 &&    <Row>
                        <Col>
                            <Space>
                                <Button
                                    style={{marginTop:'25px'}}
                                    type="primary" icon={<DownloadOutlined />}
                                    onClick={handleLoadingSelectedCities}
                                    loading={isLoadingSelectedCitiesLoading}>{t('LOAD_SELECTED_LABEL')}</Button>
                            </Space>
                        </Col>
                    </Row>}
                </Flex>
                <Flex style={{ flex: 1, minHeight: '50vh' }} align={'center'} justify={'flex-start'} vertical>
                    <Row>
                        <Typography.Title level={2}>{t('DATABASE_CONTENT')}</Typography.Title>
                    </Row>
                    <Row>
                        <LocationsTable
                            locations={localCities}
                            updateSelectedLocations={updateSelectedLocalCities}
                            localLocations={[]}
                            selectedLocations={selectedLocalCities}
                            showStatusColumn={false}/>
                    </Row>
                    {localCities.length === 0 &&    <Row>
                        <Button
                            style={{marginTop:'25px'}}
                            type="primary" icon={<DownloadOutlined />}
                            onClick={fetchLocalCities}
                            loading={isLocalCitiesLoading}>{t('FETCH_LOCAL_LABEL')}</Button>
                    </Row>}
                    {localCities.length > 0 &&    <Row>
                        <Col>
                            <Space>
                                <Button
                                    style={{marginTop:'25px'}}
                                    type="primary" icon={<DownloadOutlined />}
                                    onClick={selectAllLocal}
                                    loading={isDeletingCitiesLoading}>{t('SELECT_ALL_LABEL')}</Button>
                                <Button
                                    style={{marginTop:'25px'}}
                                    type="primary" icon={<DownloadOutlined />}
                                    onClick={deleteSelectedCities}
                                    loading={isDeletingCitiesLoading}>{t('DELETE_SELECTED_LABEL')}</Button>
                            </Space>
                        </Col>
                    </Row>}
                </Flex>
            </div>

        </>
    )
}

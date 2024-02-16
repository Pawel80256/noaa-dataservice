import {useTranslation} from "react-i18next";
import {Button, Col, Divider, Flex, notification, Row, Space, Typography} from "antd";
import {useState} from "react";
import {NOAADataType} from "../models/NOAADataType";
import {NOAALocationCategory} from "../models/NOAALocationCategory";
import {
    deleteLocalDataTypesByIds,
    getAllLocalDataTypes,
    getAllRemoteDataTypes,
    loadAllDataTypes,
    loadDataTypesByIds
} from "../services/NOAADataTypeService";
import {showErrorNotification, showSuccessNotification, showWarningNotification} from "../services/Utils";
import {
    deleteLocalLocationCategoriesByIds, getAllLocalLocationCategories,
    getAllRemoteLocationCategories,
    loadAllLocationCategories,
    loadLocationCategoriesByIds
} from "../services/NOAALocationCategoriesService";
import {DataTypesTable} from "../components/data_loader/data_types/DataTypesTable";
import {DownloadOutlined} from "@ant-design/icons";
import {LocationCategoriesTable} from "../components/data_loader/location_categories/LocationCategoriesTable";

export const LocationCategoriesLoaderView = () =>{
    const {t} = useTranslation();
    const [api, contextHolder] = notification.useNotification();

    const [remoteLocationCategories, setRemoteLocationCategories] = useState<NOAALocationCategory[]>([]);
    const [localLocationCategories, setLocalLocationCategories] = useState<NOAALocationCategory[]>([]);

    const [isRemoteLocationCategoriesLoading, setIsRemoteLocationCategoriesLoading] = useState(false);
    const [isLocalLocationCategoriesLoading, setIsLocalLocationCategoriesLoading] = useState(false)
    const [isLoadingSelectedLocationCategoriesLoading, setIsLoadingSelectedLocationCategoriesLoading] = useState(false)
    const [isLoadingAllLocationsCategoriesLoading, setIsLoadingAllLocationsCategoriesLoading] = useState(false)
    const [isDeletingLocationCategoriesLoading, setIsDeletingLocationCategoriesLoading] = useState(false)

    const [selectedRemoteLocationCategories, setSelectedRemoteLocationCategories] = useState<React.Key[]>([]);
    const [selectedLocalLocationCategories, setSelectedLocalLocationCategories] = useState<React.Key[]>([]);

    const updateSelectedRemoteLocationCategories = (keys: React.Key[]) => {
        setSelectedRemoteLocationCategories(keys);
    }

    const updateSelectedLocalLocationCategories = (keys: React.Key[]) => {
        setSelectedLocalLocationCategories(keys);
    }

    const selectAllLocal = () => {
        if (selectedLocalLocationCategories.length === localLocationCategories.length) {
            setSelectedLocalLocationCategories([]);
        } else {
            const newSelectedRowKeys = localLocationCategories.map(dt => dt.id);
            setSelectedLocalLocationCategories(newSelectedRowKeys);
        }
    };

    const selectAllRemote = () => {
        if (selectedRemoteLocationCategories.length === remoteLocationCategories.length) {
            setSelectedRemoteLocationCategories([]);
        } else {
            const newSelectedRowKeys = remoteLocationCategories.map(dt => dt.id);
            setSelectedRemoteLocationCategories(newSelectedRowKeys);
        }
    };

    const fetchRemoteLocationCategories = () => {
        setIsRemoteLocationCategoriesLoading(true);
        getAllRemoteLocationCategories().then(response => {
            setRemoteLocationCategories(response);
            setIsRemoteLocationCategoriesLoading(false);
            showSuccessNotification(t('REMOTE_FETCH_SUCCESS_LABEL'));
        }).catch(() => {
            setIsRemoteLocationCategoriesLoading(false);
            showErrorNotification(t('REMOTE_FETCH_ERROR_LABEL'));
        });
    };

    const fetchLocalLocationCategories = () => {
        setIsLocalLocationCategoriesLoading(true);
        getAllLocalLocationCategories().then(response => {
            setLocalLocationCategories(response);
            setIsLocalLocationCategoriesLoading(false);
            showSuccessNotification(t('LOCAL_FETCH_SUCCESS_LABEL'))
        }).catch(()=>{
            setIsLocalLocationCategoriesLoading(false);
            showErrorNotification(t('LOCAL_FETCH_ERROR_LABEL'))
        })
    }

    const handleLoadingSelectedLocationCategories = () => {
        const ids:string[] = selectedRemoteLocationCategories.map(key => key.toString());

        if (ids.length > 60) {
            showWarningNotification(t('LOAD_LIMIT_EXCEEDED_LABEL'))
            return;
        }

        setIsLoadingSelectedLocationCategoriesLoading(true);
        loadLocationCategoriesByIds(ids, false).then(() => {
            fetchLocalLocationCategories();
            setIsLoadingSelectedLocationCategoriesLoading(false);
            showSuccessNotification(t('LOAD_SUCCESS_LABEL'))
        }).catch(()=>{
            setIsLoadingSelectedLocationCategoriesLoading(false);
            showErrorNotification(t('LOAD_ERROR_LABEL'))
        })
    }

    const handleLoadingAllLocationCategories = () =>{
        setIsLoadingAllLocationsCategoriesLoading(true);
        loadAllLocationCategories().then(()=>{
            fetchLocalLocationCategories();
            setIsLoadingAllLocationsCategoriesLoading(false);
            showSuccessNotification(t('LOAD_SUCCESS_LABEL'))
        }).catch(()=>{
            setIsLoadingAllLocationsCategoriesLoading(false);
            showErrorNotification(t('LOAD_ERROR_LABEL'))
        })
    }

    const deleteSelectedDataTypes = () => {
        const ids:string[] = selectedLocalLocationCategories.map(key => key.toString());
        setIsDeletingLocationCategoriesLoading(true);
        deleteLocalLocationCategoriesByIds(ids).then(() => {
            const updatedSelectedDatasets = selectedLocalLocationCategories.filter(key => !ids.includes(key.toString()));
            setSelectedLocalLocationCategories(updatedSelectedDatasets);

            fetchLocalLocationCategories(/*boolean showNotification*/);
            setIsDeletingLocationCategoriesLoading(false);
            showSuccessNotification(t('DELETE_SUCCESS_LABEL'))
        }).catch(()=>{
            setIsDeletingLocationCategoriesLoading(false);
            showErrorNotification(t('DELETE_ERROR_LABEL'))
        })
    }

    return(
        <>
            {contextHolder}
            <div style={{ display: 'flex', flexDirection: 'column', height: '100vh' }}>
                <Flex style={{ flex: 1, minWidth: '50vh' }} align={'center'} justify={'flex-start'} vertical>
                    <Row>
                        <Typography.Title>{t('LOCATION_CATEGORIES')}</Typography.Title>
                    </Row>
                    <Divider />
                    <Row>
                        <Typography.Title level={2}>{t('REMOTE_CONTENT')}</Typography.Title>
                    </Row>
                    <Row>
                        <LocationCategoriesTable
                            locationCategories={remoteLocationCategories}
                            updateSelectedLocationCategories={updateSelectedRemoteLocationCategories}
                            localLocationCategories={localLocationCategories}
                            selectedLocationCategories={selectedRemoteLocationCategories}
                            showStatusColumn={true}/>
                    </Row>
                    {remoteLocationCategories.length === 0 &&    <Row>
                        <Button
                            style={{marginTop:'25px'}}
                            type="primary" icon={<DownloadOutlined />}
                            onClick={fetchRemoteLocationCategories}
                            loading={isRemoteLocationCategoriesLoading}>{t('FETCH_REMOTE_LABEL')}</Button>
                    </Row>}
                    {remoteLocationCategories.length > 0 &&    <Row>
                        <Col>
                            <Space>
                                <Button
                                    style={{marginTop:'25px'}}
                                    type="primary" icon={<DownloadOutlined />}
                                    onClick={selectAllRemote}
                                    loading={isLoadingAllLocationsCategoriesLoading}>{t('SELECT_ALL_LABEL')}</Button>
                                <Button
                                    style={{marginTop:'25px'}}
                                    type="primary" icon={<DownloadOutlined />}
                                    onClick={handleLoadingSelectedLocationCategories}
                                    loading={isLoadingSelectedLocationCategoriesLoading}>{t('LOAD_SELECTED_LABEL')}</Button>
                            </Space>
                        </Col>
                    </Row>}
                </Flex>
                <Flex style={{ flex: 1, minHeight: '50vh' }} align={'center'} justify={'flex-start'} vertical>
                    <Row>
                        <Typography.Title level={2}>{t('DATABASE_CONTENT')}</Typography.Title>
                    </Row>
                    <Row>
                        <LocationCategoriesTable
                            locationCategories={localLocationCategories}
                            updateSelectedLocationCategories={updateSelectedLocalLocationCategories}
                            localLocationCategories={localLocationCategories}
                            selectedLocationCategories={selectedLocalLocationCategories}
                            showStatusColumn={false}/>
                    </Row>
                    {localLocationCategories.length === 0 &&    <Row>
                        <Button
                            style={{marginTop:'25px'}}
                            type="primary" icon={<DownloadOutlined />}
                            onClick={fetchLocalLocationCategories}
                            loading={isLocalLocationCategoriesLoading}>{t('FETCH_LOCAL_LABEL')}</Button>
                    </Row>}
                    {localLocationCategories.length > 0 &&    <Row>
                        <Col>
                            <Space>
                                <Button
                                    style={{marginTop:'25px'}}
                                    type="primary" icon={<DownloadOutlined />}
                                    onClick={selectAllLocal}
                                    loading={isDeletingLocationCategoriesLoading}>{t('SELECT_ALL_LABEL')}</Button>
                                <Button
                                    style={{marginTop:'25px'}}
                                    type="primary" icon={<DownloadOutlined />}
                                    onClick={deleteSelectedDataTypes}
                                    loading={isDeletingLocationCategoriesLoading}>{t('DELETE_SELECTED_LABEL')}</Button>
                            </Space>
                        </Col>
                    </Row>}
                </Flex>
            </div>

        </>
    )
}
import {useTranslation} from "react-i18next";
import {Button, Col, Divider, Flex, notification, Row, Space, TableProps, Typography} from "antd";
import {useEffect, useState} from "react";
import {NOAALocationCategory} from "../models/NOAALocationCategory";
import {showErrorNotification, showSuccessNotification, showWarningNotification} from "../services/Utils";
import {
    deleteLocalLocationCategoriesByIds,
    getAllLocalLocationCategories,
    getAllRemoteLocationCategories,
    loadAllLocationCategories,
    loadLocationCategoriesByIds
} from "../services/NOAALocationCategoriesService";
import {CheckCircleOutlined, CloseCircleOutlined, DownloadOutlined} from "@ant-design/icons";
import {LocationCategoriesTable} from "../components/data_loader/location_categories/LocationCategoriesTable";
import {DataTable} from "../components/common/DataTable";

export const LocationCategoriesLoaderView = () => {
    const {t} = useTranslation();
    const [api, contextHolder] = notification.useNotification();

    const [remoteLocationCategories, setRemoteLocationCategories] = useState<NOAALocationCategory[]>([]);
    const [localLocationCategories, setLocalLocationCategories] = useState<NOAALocationCategory[]>([]);

    const [isRemoteFetchLoading, setIsRemoteFetchLoading] = useState(false);
    const [isLocalFetchLoading, setIsLocalFetchLoading] = useState(false)
    const [isLoadingLoading, setIsLoadingLoading] = useState(false)
    const [isDeletingLoading, setIsDeletingLoading] = useState(false)
    const [isAnyLoading, setIsAnyLoading] = useState(false);

    const [selectedRemoteLocationCategories, setSelectedRemoteLocationCategories] = useState<React.Key[]>([]);
    const [selectedLocalLocationCategories, setSelectedLocalLocationCategories] = useState<React.Key[]>([]);

    const [tablePagination, setTablePagination] = useState({current: 1, pageSize: 5});

    useEffect(() => {
        setIsAnyLoading(isRemoteFetchLoading || isLocalFetchLoading || isLoadingLoading || isDeletingLoading);
    }, [isRemoteFetchLoading || isLocalFetchLoading || isLoadingLoading || isDeletingLoading]);

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

    const fetchRemote = () => {
        setIsRemoteFetchLoading(true);
        getAllRemoteLocationCategories().then(response => {
            setRemoteLocationCategories(response);
            setIsRemoteFetchLoading(false);
            showSuccessNotification(t('REMOTE_FETCH_SUCCESS_LABEL'));
        }).catch(() => {
            setIsRemoteFetchLoading(false);
            showErrorNotification(t('REMOTE_FETCH_ERROR_LABEL'));
        });
    };

    const fetchLocal = () => {
        setIsLocalFetchLoading(true);
        getAllLocalLocationCategories().then(response => {
            setLocalLocationCategories(response);
            setIsLocalFetchLoading(false);
            showSuccessNotification(t('LOCAL_FETCH_SUCCESS_LABEL'))
        }).catch(() => {
            setIsLocalFetchLoading(false);
            showErrorNotification(t('LOCAL_FETCH_ERROR_LABEL'))
        })
    }

    const loadSelected = () => {
        const ids: string[] = selectedRemoteLocationCategories.map(key => key.toString());

        if (ids.length > 60) {
            showWarningNotification(t('LOAD_LIMIT_EXCEEDED_LABEL'))
            return;
        }

        setIsLoadingLoading(true);
        loadLocationCategoriesByIds(ids, false).then(() => {
            fetchLocal();
            fetchRemote()
            setIsLoadingLoading(false);
            showSuccessNotification(t('LOAD_SUCCESS_LABEL'))
        }).catch(() => {
            setIsLoadingLoading(false);
            showErrorNotification(t('LOAD_ERROR_LABEL'))
        })
    }

    const deleteSelected = () => {
        const ids: string[] = selectedLocalLocationCategories.map(key => key.toString());
        setIsDeletingLoading(true);
        deleteLocalLocationCategoriesByIds(ids).then(() => {
            const updatedSelectedDatasets = selectedLocalLocationCategories.filter(key => !ids.includes(key.toString()));
            setSelectedLocalLocationCategories(updatedSelectedDatasets);
            fetchLocal(/*boolean showNotification*/);
            fetchRemote()
            setIsDeletingLoading(false);
            showSuccessNotification(t('DELETE_SUCCESS_LABEL'))
        }).catch(() => {
            setIsDeletingLoading(false);
            showErrorNotification(t('DELETE_ERROR_LABEL'))
        })
    }

    const localColumns: TableProps<NOAALocationCategory>['columns'] = [
        {
            title: t('INDEX_COLUMN'),
            key: 'index',
            render: (value, item, index) => (tablePagination.current - 1) * tablePagination.pageSize + index + 1
        },
        {
            title: t('IDENTIFIER_COLUMN'),
            dataIndex: 'id',
            key: 'id'
        },
        {
            title: t('NAME_COLUMN'),
            dataIndex: 'name',
            key: 'name'
        }
    ]

    const remoteColumns: TableProps<NOAALocationCategory>['columns'] = [
        ...localColumns,
        {
            title: t('STATUS_COLUMN'),
            dataIndex: 'loaded',
            key: 'loaded',
            render: (text, record) => record && record.loaded ? <CheckCircleOutlined style={{color: 'green'}}/> :
                <CloseCircleOutlined style={{color: 'red'}}/>
        }
    ]

    const searchableColumns: string [] = ["id", "name"];

    return (
        <>
            {contextHolder}
            <div style={{display: 'flex', flexDirection: 'column', height: '100vh'}}>
                <Flex style={{flex: 1, minWidth: '50vh'}} align={'center'} justify={'flex-start'} vertical>
                    <Row>
                        <Typography.Title>{t('LOCATION_CATEGORIES')}</Typography.Title>
                    </Row>
                    <Divider/>
                    <Row>
                        <Typography.Title level={2}>{t('REMOTE_CONTENT')}</Typography.Title>
                    </Row>
                    <Row>
                        <DataTable
                            columns={remoteColumns}
                            data={remoteLocationCategories}
                            selectedData={selectedRemoteLocationCategories}
                            updateSelectedData={setSelectedRemoteLocationCategories}
                            pagination={tablePagination}
                            setPagination={setTablePagination}
                            isAnyLoading={isAnyLoading}/>
                    </Row>
                    <Row>
                        <Col>
                            <Space>
                                <Button
                                    style={{marginTop: '25px'}}
                                    type="primary" icon={<DownloadOutlined/>}
                                    onClick={fetchRemote}
                                    loading={isRemoteFetchLoading}>{t('FETCH_REMOTE_LABEL')}
                                </Button>
                                {remoteLocationCategories.length > 0 &&
                                    <>
                                        <Button
                                            style={{marginTop: '25px'}}
                                            type="primary" icon={<DownloadOutlined/>}
                                            onClick={selectAllRemote}
                                        >{t('SELECT_ALL_LABEL')}
                                        </Button>
                                        <Button
                                            style={{marginTop: '25px'}}
                                            type="primary" icon={<DownloadOutlined/>}
                                            onClick={loadSelected}
                                            loading={isLoadingLoading}>{t('LOAD_SELECTED_LABEL')}
                                        </Button>
                                    </>
                               }
                            </Space>
                        </Col>

                    </Row>

                </Flex>
                <Flex style={{flex: 1, minHeight: '50vh'}} align={'center'} justify={'flex-start'} vertical>
                    <Row>
                        <Typography.Title level={2}>{t('DATABASE_CONTENT')}</Typography.Title>
                    </Row>
                    <Row>
                        <DataTable
                            columns={localColumns}
                            data={localLocationCategories}
                            selectedData={selectedLocalLocationCategories}
                            updateSelectedData={setSelectedLocalLocationCategories}
                            pagination={tablePagination}
                            setPagination={setTablePagination}
                            isAnyLoading={isAnyLoading}/>
                    </Row>
                    {localLocationCategories.length === 0 && <Row>
                        <Button
                            style={{marginTop: '25px'}}
                            type="primary" icon={<DownloadOutlined/>}
                            onClick={fetchLocal}
                            loading={isLocalFetchLoading}>{t('FETCH_LOCAL_LABEL')}</Button>
                    </Row>}
                    {localLocationCategories.length > 0 && <Row>
                        <Col>
                            <Space>
                                <Button
                                    style={{marginTop: '25px'}}
                                    type="primary" icon={<DownloadOutlined/>}
                                    onClick={selectAllLocal}
                                    loading={isDeletingLoading}>{t('SELECT_ALL_LABEL')}</Button>
                                <Button
                                    style={{marginTop: '25px'}}
                                    type="primary" icon={<DownloadOutlined/>}
                                    onClick={deleteSelected}
                                    loading={isDeletingLoading}>{t('DELETE_SELECTED_LABEL')}</Button>
                            </Space>
                        </Col>
                    </Row>}
                </Flex>
            </div>

        </>
    )
}
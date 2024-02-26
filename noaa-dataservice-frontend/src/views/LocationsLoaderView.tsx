import {useTranslation} from "react-i18next";
import {Button, Col, Divider, Flex, notification, Row, Space, TableProps, Typography} from "antd";
import {useEffect, useState} from "react";
import {NOAADataType} from "../models/NOAADataType";
import {NOAALocation} from "../models/NOAALocation";
import {getAllRemoteLocationCategories} from "../services/NOAALocationCategoriesService";
import {showErrorNotification, showSuccessNotification} from "../services/Utils";
import {
    deleteLocationsByIds,
    getLocalLocations,
    getRemoteLocations,
    loadLocationsByIds
} from "../services/NOAALocationService";
import {deleteLocalDataTypesByIds, getAllLocalDataTypes, loadDataTypesByIds} from "../services/NOAADataTypeService";
import {CheckCircleOutlined, CloseCircleOutlined, DownloadOutlined} from "@ant-design/icons";
import {LocationsTable} from "../components/data_loader/locations/LocationsTable";
import {DataTable} from "../components/common/DataTable";

export interface LocationsLoaderViewProps{
    locationCategoryId:string
}
export const LocationsLoaderView = ({locationCategoryId}: LocationsLoaderViewProps) => {
    const {t} = useTranslation();
    const [api, contextHolder] = notification.useNotification();

    const [remoteData, setRemoteData] = useState<NOAALocation[]>([]);
    const [localData, setLocalData] = useState<NOAALocation[]>([]);

    const [isRemoteFetchLoading, setIsRemoteFetchLoading] = useState(false);
    const [isLocalFetchLoading, setIsLocalFetchLoading] = useState(false)
    const [isLoadingLoading, setIsLoadingLoading] = useState(false)
    const [isDeletingLoading, setIsDeletingLoading] = useState(false)
    const [isAnyLoading, setIsAnyLoading] = useState(false);

    const [selectedRemoteData, setSelectedRemoteData] = useState<React.Key[]>([]);
    const [selectedLocalData, setSelectedLocalData] = useState<React.Key[]>([]);

    const [tablePagination, setTablePagination] = useState({current: 1, pageSize: 5});

    useEffect(() => {
        setIsAnyLoading(isRemoteFetchLoading || isLocalFetchLoading || isLoadingLoading || isDeletingLoading);
    }, [isRemoteFetchLoading || isLocalFetchLoading || isLoadingLoading || isDeletingLoading]);

    useEffect(() => {
        setRemoteData([]);
        setSelectedRemoteData([]);
        setLocalData([]);
        setSelectedLocalData([]);
    }, [locationCategoryId]);

    const selectAllLocal = () => {
        if (selectedLocalData.length === localData.length) {
            setSelectedLocalData([]);
        } else {
            const newSelectedRowKeys = localData.map(dt => dt.id);
            setSelectedLocalData(newSelectedRowKeys);
        }
    };

    const selectAllRemote = () => {
        if (selectedRemoteData.length === selectedRemoteData.length) {
            setSelectedRemoteData([]);
        } else {
            const newSelectedRowKeys = remoteData.map(dt => dt.id);
            setSelectedRemoteData(newSelectedRowKeys);
        }
    };
    const fetchRemote = () => {
        setIsRemoteFetchLoading(true);
        getRemoteLocations(locationCategoryId).then(response => {
            setRemoteData(response);
            setIsRemoteFetchLoading(false);
            showSuccessNotification(t('REMOTE_FETCH_SUCCESS_LABEL'));
        }).catch(() => {
            setIsRemoteFetchLoading(false);
            showErrorNotification(t('REMOTE_FETCH_ERROR_LABEL'));
        });
    };

    const fetchLocal = () => {
        setIsLocalFetchLoading(true);
        getLocalLocations(locationCategoryId).then(response => {
            setLocalData(response);
            setIsLocalFetchLoading(false);
            showSuccessNotification(t('LOCAL_FETCH_SUCCESS_LABEL'))
        }).catch(() => {
            setIsLocalFetchLoading(false);
            showErrorNotification(t('LOCAL_FETCH_ERROR_LABEL'))
        })
    }

    const loadSelected = () => {
        const ids: string[] = selectedRemoteData.map(key => key.toString());
        setIsLoadingLoading(true);
        loadLocationsByIds(locationCategoryId, ids).then(() => {
            fetchLocal();
            fetchRemote();
            setIsLoadingLoading(false);
            showSuccessNotification(t('LOAD_SUCCESS_LABEL'))
        }).catch(() => {
            setIsLoadingLoading(false);
            showErrorNotification(t('LOAD_ERROR_LABEL'))
        })
    }

    const deleteSelected = () => {
        const ids: string[] = selectedLocalData.map(key => key.toString());
        setIsDeletingLoading(true);
        deleteLocationsByIds(ids).then(() => {
            const updateSelectedData = selectedLocalData.filter(key => !ids.includes(key.toString()));
            setSelectedLocalData(updateSelectedData)

            fetchLocal(/*boolean showNotification*/);
            fetchRemote()
            setIsDeletingLoading(false);
            showSuccessNotification(t('DELETE_SUCCESS_LABEL'))
        }).catch(() => {
            setIsDeletingLoading(false);
            showErrorNotification(t('DELETE_ERROR_LABEL'))
        })
    }

    const localColumns: TableProps<NOAALocation>['columns'] = [
        {
            title: t('INDEX_COLUMN'),
            key: 'index',
            render: (value, item, index) => (tablePagination.current - 1) * tablePagination.pageSize + index + 1
        },
        {
            title: t('IDENTIFIER_COLUMN'),
            dataIndex: 'id',
            key: 'id',
            sorter: (a, b) => a.id.localeCompare(b.id),
        },
        {
            title: t('NAME_COLUMN'),
            dataIndex: 'name',
            key: 'name',
            sorter: (a, b) => a.name.localeCompare(b.name),
        },
        {
            title: t('DATA_COVERAGE_COLUMN'),
            dataIndex: 'dataCoverage',
            key: 'dataCoverage',
            sorter: (a, b) => a.dataCoverage - b.dataCoverage,
        },
        {
            title: t('MIN_DATE_COLUMN'),
            dataIndex: 'mindate',
            key: 'mindate',
            render: (text, record) => record && record.minDate ? new Date(record.minDate).toISOString().split('T')[0] : '',
            sorter: (a, b) => new Date(a.minDate).getTime() - new Date(b.minDate).getTime(),
        },
        {
            title: t('MAX_DATE_COLUMN'),
            dataIndex: 'maxdate',
            key: 'maxdate',
            render: (text, record) => record && record.maxDate ? new Date(record.maxDate).toISOString().split('T')[0] : '',
            sorter: (a, b) => new Date(a.maxDate).getTime() - new Date(b.maxDate).getTime(),
        },
        {
            title: t('PARENT_COLUMN'),
            dataIndex: 'parentId',
            key: 'parentId',
            sorter: (a, b) => a.parentId.localeCompare(b.parentId),
        },
    ];

    const remoteColumns: TableProps<NOAALocation>['columns'] = [
        ...localColumns,
        {
            title: t('STATUS_COLUMN'),
            dataIndex: 'loaded',
            key: 'loaded',
            render: (text, record) => record && record.loaded ? <CheckCircleOutlined style={{color: 'green'}}/> :
                <CloseCircleOutlined style={{color: 'red'}}/>
        }
    ]

    const searchableColumns: string [] = ["id", "name", "dataCoverage", "mindate", "maxdate", "parentId", "loaded"];
    const getTitleLabel = () => {
        switch (locationCategoryId){
            case "CNTRY":
                return t('COUNTRIES');
            case "CITY":
                return t('CITIES');
            case "ST":
                return t("STATES");
        }
        return "ERROR"
    }

    return(
        <>
            {contextHolder}
            <div style={{ display: 'flex', flexDirection: 'column', height: '100vh' }}>
                <Flex style={{ flex: 1, minWidth: '50vh' }} align={'center'} justify={'flex-start'} vertical>
                    <Row>
                        <Typography.Title>{getTitleLabel()}</Typography.Title>
                    </Row>
                    <Divider />
                    <Row>
                        <Typography.Title level={2}>{t('REMOTE_CONTENT')}</Typography.Title>
                    </Row>
                    <Row>
                        <DataTable
                            columns={remoteColumns}
                            data={remoteData}
                            selectedData={selectedRemoteData}
                            updateSelectedData={setSelectedRemoteData}
                            pagination={tablePagination}
                            setPagination={setTablePagination}
                            searchableColumns={searchableColumns}
                            isAnyLoading={isAnyLoading}
                            />
                    </Row>
                        <Row>
                        <Col>
                            <Space>
                                <Button
                                    style={{marginTop:'25px'}}
                                    type="primary" icon={<DownloadOutlined />}
                                    onClick={fetchRemote}
                                    loading={isRemoteFetchLoading}>{t('FETCH_REMOTE_LABEL')}
                                </Button>
                                {remoteData.length > 0 &&
                                    <>
                                        <Button
                                            style={{marginTop:'25px'}}
                                            type="primary" icon={<DownloadOutlined />}
                                            onClick={selectAllRemote}
                                        >{t('SELECT_ALL_LABEL')}
                                        </Button>
                                        <Button
                                            style={{marginTop:'25px'}}
                                            type="primary" icon={<DownloadOutlined />}
                                            onClick={loadSelected}
                                            loading={isLoadingLoading}>{t('LOAD_SELECTED_LABEL')}
                                        </Button>
                                    </>
                            }
                            </Space>
                        </Col>
                    </Row>
                </Flex>
                <Flex style={{ flex: 1, minHeight: '50vh' }} align={'center'} justify={'flex-start'} vertical>
                    <Row>
                        <Typography.Title level={2}>{t('DATABASE_CONTENT')}</Typography.Title>
                    </Row>
                    <Row>
                        <DataTable
                            columns={localColumns}
                            data={localData}
                            selectedData={selectedLocalData}
                            updateSelectedData={setSelectedLocalData}
                            pagination={tablePagination}
                            setPagination={setTablePagination}
                            searchableColumns={searchableColumns}
                            isAnyLoading={isAnyLoading}
                        />
                    </Row>
                    {localData.length === 0 &&    <Row>
                        <Button
                            style={{marginTop:'25px'}}
                            type="primary" icon={<DownloadOutlined />}
                            onClick={fetchLocal}
                            loading={isLocalFetchLoading}>{t('FETCH_LOCAL_LABEL')}</Button>
                    </Row>}
                    {localData.length > 0 &&    <Row>
                        <Col>
                            <Space>
                                <Button
                                    style={{marginTop:'25px'}}
                                    type="primary" icon={<DownloadOutlined />}
                                    onClick={selectAllLocal}
                                    >{t('SELECT_ALL_LABEL')}</Button>
                                <Button
                                    style={{marginTop:'25px'}}
                                    type="primary" icon={<DownloadOutlined />}
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
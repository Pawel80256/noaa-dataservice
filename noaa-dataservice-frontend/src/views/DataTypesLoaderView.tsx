import {useTranslation} from "react-i18next";
import {Button, Col, Divider, Flex, notification, Row, Space, TableProps, Typography} from "antd";
import {useState} from "react";
import {NOAADataType} from "../models/NOAADataType";
import {showErrorNotification, showSuccessNotification} from "../services/Utils";
import {
    deleteLocalDataTypesByIds,
    getAllLocalDataTypes,
    getAllRemoteDataTypes,
    loadAllDataTypes,
    loadDataTypesByIds
} from "../services/NOAADataTypeService";
import {CheckCircleOutlined, CloseCircleOutlined, DownloadOutlined} from "@ant-design/icons";
import {DataTable} from "../components/common/DataTable";

export const DataTypesLoaderView = () => {
    const {t} = useTranslation();
    const [api, contextHolder] = notification.useNotification();

    const [remoteDataTypes, setRemoteDataTypes] = useState<NOAADataType[]>([]);
    const [localDataTypes, setLocalDataTypes] = useState<NOAADataType[]>([]);

    const [isRemoteDataTypesLoading, setIsRemoteDataTypesLoading] = useState(false);
    const [isLocalDataTypesLoading, setIsLocalDataTypesLoading] = useState(false)
    const [isLoadingSelectedDataTypesLoading, setIsLoadingSelectedDataTypesLoading] = useState(false)
    const [isDeletingDataTypesLoading, setIsDeletingDataTypesLoading] = useState(false)

    const [selectedRemoteDataTypes, setSelectedRemoteDataTypes] = useState<React.Key[]>([]);
    const [selectedLocalDataTypes, setSelectedLocalDataTypes] = useState<React.Key[]>([]);

    const [tablePagination, setTablePagination] = useState({current: 1, pageSize: 5});

    const updateSelectedRemoteDataTypes = (keys: React.Key[]) => {
        setSelectedRemoteDataTypes(keys)
    }
    const updateSelectedLocalDataTypes = (keys: React.Key[]) => {
        setSelectedLocalDataTypes(keys)
    }

    const selectAllLocal = () => {
        if (selectedLocalDataTypes.length === localDataTypes.length) {
            setSelectedLocalDataTypes([]);
        } else {
            const newSelectedRowKeys = localDataTypes.map(dt => dt.id);
            setSelectedLocalDataTypes(newSelectedRowKeys);
        }
    };

    const selectAllRemote = () => {
        if (selectedRemoteDataTypes.length === remoteDataTypes.length) {
            setSelectedRemoteDataTypes([]);
        } else {
            const newSelectedRowKeys = remoteDataTypes.map(dt => dt.id);
            setSelectedRemoteDataTypes(newSelectedRowKeys);
        }
    };

    const fetchRemoteDataTypes = () => {
        setIsRemoteDataTypesLoading(true);
        getAllRemoteDataTypes().then(response => {
            setRemoteDataTypes(response);
            setIsRemoteDataTypesLoading(false);
            showSuccessNotification(t('REMOTE_FETCH_SUCCESS_LABEL'));
        }).catch(() => {
            setIsRemoteDataTypesLoading(false);
            showErrorNotification(t('REMOTE_FETCH_ERROR_LABEL'));
        });
    };

    const fetchLocalDataTypes = () => {
        setIsLocalDataTypesLoading(true);
        getAllLocalDataTypes().then(response => {
            setLocalDataTypes(response);
            setIsLocalDataTypesLoading(false);
            showSuccessNotification(t('LOCAL_FETCH_SUCCESS_LABEL'))
        }).catch(() => {
            setIsLocalDataTypesLoading(false);
            showErrorNotification(t('LOCAL_FETCH_ERROR_LABEL'))
        })
    }

    const handleLoadingSelectedDataTypes = () => {
        const ids: string[] = selectedRemoteDataTypes.map(key => key.toString());
        setIsLoadingSelectedDataTypesLoading(true);
        loadDataTypesByIds(ids).then(() => {
            fetchLocalDataTypes();
            fetchRemoteDataTypes();
            setIsLoadingSelectedDataTypesLoading(false);
            showSuccessNotification(t('LOAD_SUCCESS_LABEL'))
        }).catch(() => {
            setIsLoadingSelectedDataTypesLoading(false);
            showErrorNotification(t('LOAD_ERROR_LABEL'))
        })
    }

    const deleteSelectedDataTypes = () => {
        const ids: string[] = selectedLocalDataTypes.map(key => key.toString());
        setIsDeletingDataTypesLoading(true);
        deleteLocalDataTypesByIds(ids).then(() => {
            const updatedSelectedDatTypes = selectedLocalDataTypes.filter(key => !ids.includes(key.toString()));
            setSelectedLocalDataTypes(updatedSelectedDatTypes)

            fetchLocalDataTypes(/*boolean showNotification*/);
            fetchRemoteDataTypes()
            setIsDeletingDataTypesLoading(false);
            showSuccessNotification(t('DELETE_SUCCESS_LABEL'))
        }).catch(() => {
            setIsDeletingDataTypesLoading(false);
            showErrorNotification(t('DELETE_ERROR_LABEL'))
        })
    }

    const localColumns: TableProps<NOAADataType>['columns'] = [
        {
            title: t('INDEX_COLUMN'),
            key: 'index',
            render: (value, item, index) => (tablePagination.current - 1) * tablePagination.pageSize + index + 1
        },
        {
            title: t('IDENTIFIER_COLUMN'),
            dataIndex: 'id',
            key: 'id',
            sorter: (a, b) => a.id.localeCompare(b.id)
        },
        {
            title: t('NAME_COLUMN'),
            dataIndex: 'name',
            key: 'name',
            sorter: (a, b) => a.name.localeCompare(b.name)
        },
        {
            title: t('DATA_COVERAGE_COLUMN'),
            dataIndex: 'dataCoverage',
            key: 'dataCoverage',
            sorter: (a, b) => a.dataCoverage - b.dataCoverage,
        },
        {
            title: t('MIN_DATE_COLUMN'),
            dataIndex: 'minDate',
            key: 'minDate',
            render: (text, record) => record && record.minDate ? new Date(record.minDate).toISOString().split('T')[0] : '',
            sorter: (a, b) => new Date(a.minDate).getTime() - new Date(b.minDate).getTime(),
        },
        {
            title: t('MAX_DATE_COLUMN'),
            dataIndex: 'maxDate',
            key: 'maxDate',
            render: (text, record) => record && record.maxDate ? new Date(record.maxDate).toISOString().split('T')[0] : '',
            sorter: (a, b) => new Date(a.maxDate).getTime() - new Date(b.maxDate).getTime(),
        }
    ]

    const remoteColumns: TableProps<NOAADataType>['columns'] = [
        ...localColumns,
        {
            title: t('STATUS_COLUMN'),
            dataIndex: 'loaded',
            key: 'loaded',
            render: (text, record) => record && record.loaded ? <CheckCircleOutlined style={{color: 'green'}}/> :
                <CloseCircleOutlined style={{color: 'red'}}/>
        }
    ]

    const searchableColumns: string [] = ["id", "name", "dataCoverage", "minDate", "maxDate"];

    return (
        <>
            {contextHolder}
            <div style={{display: 'flex', flexDirection: 'column', height: '100vh'}}>
                <Flex style={{flex: 1, minWidth: '50vh'}} align={'center'} justify={'flex-start'} vertical>
                    <Row>
                        <Typography.Title>{t('DATA_TYPES')}</Typography.Title>
                    </Row>
                    <Divider/>
                    <Row>
                        <Typography.Title level={2}>{t('REMOTE_CONTENT')}</Typography.Title>
                    </Row>
                    <Row>
                        <DataTable
                            columns={remoteColumns}
                            data={remoteDataTypes}
                            selectedData={selectedRemoteDataTypes}
                            updateSelectedData={updateSelectedRemoteDataTypes}
                            pagination={tablePagination}
                            setPagination={setTablePagination}
                            searchableColumns={searchableColumns}
                        />
                    </Row>
                    <Row>
                        <Col>
                            <Space>
                                <Button
                                    style={{marginTop: '25px'}}
                                    type="primary" icon={<DownloadOutlined/>}
                                    onClick={fetchRemoteDataTypes}
                                    loading={isRemoteDataTypesLoading}>{t('FETCH_REMOTE_LABEL')}</Button>
                                {remoteDataTypes.length > 0 &&
                                    <>
                                        <Button
                                            style={{marginTop: '25px'}}
                                            type="primary" icon={<DownloadOutlined/>}
                                            onClick={selectAllRemote}>{t('SELECT_ALL_LABEL')}</Button>
                                        <Button
                                            style={{marginTop: '25px'}}
                                            type="primary" icon={<DownloadOutlined/>}
                                            onClick={handleLoadingSelectedDataTypes}
                                            loading={isLoadingSelectedDataTypesLoading}>{t('LOAD_SELECTED_LABEL')}</Button>

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
                    <DataTable
                        columns={localColumns}
                        data={localDataTypes}
                        selectedData={selectedLocalDataTypes}
                        updateSelectedData={updateSelectedLocalDataTypes}
                        pagination={tablePagination}
                        setPagination={setTablePagination}
                        searchableColumns={searchableColumns}
                    />
                    {localDataTypes.length === 0 && <Row>
                        <Button
                            style={{marginTop: '25px'}}
                            type="primary" icon={<DownloadOutlined/>}
                            onClick={fetchLocalDataTypes}
                            loading={isLocalDataTypesLoading}>{t('FETCH_LOCAL_LABEL')}</Button>
                    </Row>}
                    {localDataTypes.length > 0 && <Row>
                        <Col>
                            <Space>
                                <Button
                                    style={{marginTop: '25px'}}
                                    type="primary" icon={<DownloadOutlined/>}
                                    onClick={selectAllLocal}
                                    loading={isDeletingDataTypesLoading}>{t('SELECT_ALL_LABEL')}</Button>
                                <Button
                                    style={{marginTop: '25px'}}
                                    type="primary" icon={<DownloadOutlined/>}
                                    onClick={deleteSelectedDataTypes}
                                    loading={isDeletingDataTypesLoading}>{t('DELETE_SELECTED_LABEL')}</Button>
                            </Space>
                        </Col>
                    </Row>}
                </Flex>
            </div>

        </>
    )
}
import {Button, Col, Divider, Flex, Row, Space, TableProps, Typography} from "antd";
import {useTranslation} from "react-i18next";
import {
    BankOutlined,
    CheckCircleOutlined,
    CloseCircleOutlined,
    DownloadOutlined,
    GlobalOutlined,
    TableOutlined
} from "@ant-design/icons";
import {useEffect, useState} from "react";
import {NOAALocation} from "../models/NOAALocation";
import {LocationsTable} from "../components/data_loader/locations/LocationsTable";
import {showErrorNotification, showSuccessNotification} from "../services/Utils";
import {NOAAStation} from "../models/NOAAStation";
import {
    deleteStationsByIds,
    getAllLocalStations,
    getRemoteStationsByLocationId,
    loadByIdsAndLocationId
} from "../services/NOAAStationService";
import {StationsTable} from "../components/data_loader/stations/StationsTable";
import {getLocalLocations} from "../services/NOAALocationService";
import {DataTable} from "../components/common/DataTable";

export const StationsLoaderView = () => {
    //wybieranie kategorii lokalizacji do szukania (kraje / miasta / stany)
    const {t} = useTranslation();
    const [localLocations, setLocalLocations] = useState<NOAALocation[]>([]);

    const [localStations, setLocalStations] = useState<NOAAStation[]>([]);
    const [remoteStations, setRemoteStations] = useState<NOAAStation[]>([]);

    const [selectedLocation, setSelectedLocation] = useState<React.Key | null>(null);
    const [selectedLocalStations, setSelectedLocalStations] = useState<React.Key[]>([]);
    const [selectedRemoteStations, setSelectedRemoteStations] = useState<React.Key[]>([]);

    const [isLocalLocationsLoading, setIsLocalLocationsLoading] = useState<boolean>(false)
    const [isRemoteStationsLoading, setIsRemoteStationsLoading] = useState<boolean>(false);
    const [isLocalStationsLoading, setIsLocalStationsLoading] = useState<boolean>(false);
    const [isLoadingLoading, setIsLoadingLoading] = useState<boolean>(false);
    const [isDeletingLoading, setIsDeletingLoading] = useState<boolean>(false);
    const [isAnyLoading, setIsAnyLoading] = useState<boolean>(false)

    const [tablePagination, setTablePagination] = useState({current: 1, pageSize: 5});

    useEffect(() => {
        setIsAnyLoading(isRemoteStationsLoading || isLocalStationsLoading || isLoadingLoading || isDeletingLoading);
    }, [isRemoteStationsLoading || isRemoteStationsLoading || isLoadingLoading || isDeletingLoading]);

    const selectAllLocal = () => {
        if (selectedLocalStations.length === localStations.length) {
            setSelectedLocalStations([]);
        } else {
            const newSelectedRowKeys = localStations.map(dt => dt.id);
            setSelectedLocalStations(newSelectedRowKeys);
        }
    };

    const selectAllRemote = () => {
        if (selectedRemoteStations.length === remoteStations.length) {
            setSelectedRemoteStations([]);
        } else {
            const newSelectedRowKeys = remoteStations.map(dt => dt.id);
            setSelectedRemoteStations(newSelectedRowKeys);
        }
    };

    const fetchRemoteStationsByLocationId = (locationId: string) => {
        setIsRemoteStationsLoading(true);
        getRemoteStationsByLocationId(locationId).then(response => {
            setRemoteStations(response);
            setIsRemoteStationsLoading(false);
            showSuccessNotification(t('REMOTE_FETCH_SUCCESS_LABEL'));
        }).catch(() => {
            setIsRemoteStationsLoading(false);
            showErrorNotification(t('REMOTE_FETCH_ERROR_LABEL'));
        });
    }

    const fetchLocalStations = () => {
        setIsLocalStationsLoading(true);
        getAllLocalStations().then(response => {
            setLocalStations(response);
            setIsLocalStationsLoading(false);
            showSuccessNotification(t('LOCAL_FETCH_SUCCESS_LABEL'));
        }).catch(()=>{
            setIsLocalStationsLoading(false);
            showErrorNotification(t('LOCAL_FETCH_ERROR_LABEL'))
        })
    }

    const fetchLocalLocations = (locationCategory: string) => {
        setIsLocalLocationsLoading(true);
        getLocalLocations(locationCategory).then(res => {
            setLocalLocations(res);
            setIsLocalLocationsLoading(false);
            showSuccessNotification(t('LOCAL_FETCH_SUCCESS_LABEL'));
        }).catch(() => {
            setIsLocalLocationsLoading(false);
            showErrorNotification(t('LOCAL_FETCH_ERROR_LABEL'));
        });
    }

    const handleLoading = () => {
        const ids:string[] = selectedRemoteStations.map(key => key.toString());
        const locationId = selectedLocation!.toString();

        setIsLoadingLoading(true);
        loadByIdsAndLocationId(locationId,ids).then(() => {
            fetchLocalStations();
            fetchRemoteStationsByLocationId(locationId);
            setIsLoadingLoading(false);
            showSuccessNotification(t('LOAD_SUCCESS_LABEL'))
        }).catch(() => {
            setIsLoadingLoading(false);
            showErrorNotification(t('LOAD_ERROR_LABEL'))
        })
    }

    const deleteSelectedLocalStations = () => {
        const ids:string[] = selectedLocalStations.map(key => key.toString());
        const locationId = selectedLocation!.toString();

        setIsDeletingLoading(true);
        deleteStationsByIds(ids).then(() => {
            const updatedSelectedStations = selectedLocalStations.filter(key => !ids.includes(key.toString()));
            setSelectedLocalStations(updatedSelectedStations);
            fetchLocalStations();
            fetchRemoteStationsByLocationId(locationId);
            setIsDeletingLoading(false);
            showSuccessNotification(t('DELETE_SUCCESS_LABEL'))
        }).catch(()=>{
            setIsDeletingLoading(false);
            showErrorNotification(t('DELETE_ERROR_LABEL'))
        })
    }

    const localLocationColumns: TableProps<NOAALocation>['columns'] = [
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

    const searchableLocationsColumns: string [] = ["id", "name", "dataCoverage", "mindate", "maxdate", "parentId"];


    const localStationsColumns: TableProps<NOAAStation>['columns'] = [
        {
            title: t('INDEX_COLUMN'),
            key: 'index',
            render: (value, item, index) => (tablePagination.current - 1) * tablePagination.pageSize + index + 1
        },
        {
            title: t('IDENTIFIER_COLUMN'),
            dataIndex:'id',
            key:'id',
            sorter: (a, b) => a.id.localeCompare(b.id)
        },
        {
            title: t('NAME_COLUMN'),
            dataIndex:'name',
            key:'name',
            sorter: (a, b) => a.name.localeCompare(b.name)
        },
        {
            title: t('DATA_COVERAGE_COLUMN'),
            dataIndex:'dataCoverage',
            key:'dataCoverage',
            sorter: (a, b) => a.dataCoverage - b.dataCoverage,
        },
        {
            title: t('MIN_DATE_COLUMN'),
            dataIndex:'minDate',
            key:'minDate',
            render: (text, record) => record && record.minDate ? new Date(record.minDate).toISOString().split('T')[0] : '',
            sorter: (a, b) => new Date(a.minDate).getTime() - new Date(b.minDate).getTime(),
        },
        {
            title: t('MAX_DATE_COLUMN'),
            dataIndex:'maxDate',
            key:'maxDate',
            render: (text, record) => record && record.maxDate ? new Date(record.maxDate).toISOString().split('T')[0] : '',
            sorter: (a, b) => new Date(a.maxDate).getTime() - new Date(b.maxDate).getTime(),
        },
        {
            title: t('ELEVATION_COLUMN'),
            dataIndex: 'elevation',
            key: 'elevation',
            sorter: (a, b) => a.elevation - b.elevation,
        },
        {
            title: t('ELEVATION_UNIT_COLUMN'),
            dataIndex: 'elevationUnit',
            key: 'elevationUnit',
            sorter:(a,b) => a.elevationUnit.localeCompare(b.elevationUnit)
        },
        {
            title: t('LATITUDE_COLUMN'),
            dataIndex: 'latitude',
            key: 'latitude',
            sorter:(a,b) => a.latitude - b.latitude
        },
        {
            title: t('LONGITUDE_COLUMN'),
            dataIndex: 'longitude',
            key: 'longitude',
            sorter:(a,b) => a.longitude - b.longitude
        },
        {
            title: t('LOCATION_COLUMN'),
            dataIndex: 'locationId',
            key: 'locationId',
            sorter:(a,b) => a.locationId.localeCompare(b.locationId)
        },
    ]

    const remoteStationsColumns: TableProps<NOAAStation>['columns'] = [
        ...localStationsColumns,
        {
            title: t('STATUS_COLUMN'),
            dataIndex: 'loaded',
            key: 'loaded',
            render: (text, record) => record && record.loaded ? <CheckCircleOutlined style={{color: 'green'}}/> :
                <CloseCircleOutlined style={{color: 'red'}}/>
        }
    ]

    const searchableStationColumns = ["id","name","dataCoverage","minDate","maxDate","elevation","elevationUnit","latitude","longitude","locationId","loaded"]

    return (
        <>
            <div style={{display: 'flex', flexDirection: 'column', height: '100vh'}}>
                <Flex style={{flex: 1, minWidth: '50vh'}} align={'center'} justify={'flex-start'} vertical>
                    <Row>
                        <Typography.Title level={2}>{t('LOCAL_LOCATIONS_LABEL')}</Typography.Title>
                    </Row>
                    <Row style={{marginTop: "2%"}}>
                        <Col>
                            <Space>
                                <Button
                                    type="primary"
                                    size="large"
                                    onClick={() => fetchLocalLocations("CNTRY")}
                                    icon={<GlobalOutlined/>}>{t('SEARCH_COUNTRIES_LABEL')}</Button>
                                <Button
                                    type="primary"
                                    size="large"
                                    onClick={() => fetchLocalLocations("CITY")}
                                    icon={<BankOutlined/>}>{t('SEARCH_CITIES_LABEL')}</Button>
                                <Button
                                    type="primary"
                                    size="large"
                                    onClick={() => fetchLocalLocations("ST")}
                                    icon={<TableOutlined/>}>{t('SEARCH_STATES_LABEL')}</Button>
                            </Space>
                        </Col>
                    </Row>
                    <Row style={{marginTop: "5%"}}>
                        <DataTable
                            columns={localLocationColumns}
                            data={localLocations}
                            updateSelectedData={(keys) => {
                                setSelectedLocation(keys.length > 0 ? keys[0] : null);
                            }}
                            pagination={tablePagination}
                            setPagination={setTablePagination}
                            singleSelect={true}
                            isAnyLoading={isAnyLoading}
                            searchableColumns={searchableLocationsColumns}
                            />
                    </Row>
                    {localLocations.length > 0 &&
                        <Row>
                            <Button
                                type="primary"
                                size="large"
                                style={{marginTop: '25px'}}
                                disabled={!selectedLocation}
                                loading={isRemoteStationsLoading}
                                onClick={() => {
                                    if (selectedLocation) {
                                        fetchRemoteStationsByLocationId(selectedLocation as string);
                                    }
                                }}
                                icon={<DownloadOutlined/>}>{t('FIND_STATIONS_LABEL')}</Button>
                        </Row>
                    }
                    <Divider style={{marginTop: '50px'}}/>
                    <Row>
                        <Typography.Title level={2}>{t('REMOTE_STATIONS_LABEL')}</Typography.Title>
                    </Row>
                    <Row>
                        <DataTable
                            columns={remoteStationsColumns}
                            data={remoteStations}
                            updateSelectedData={setSelectedRemoteStations}
                            pagination={tablePagination}
                            setPagination={setTablePagination}
                            isAnyLoading={isAnyLoading}
                            searchableColumns={searchableStationColumns}
                            />
                    </Row>
                    {remoteStations.length > 0 &&
                        <Row>
                        <Col>
                            <Space>
                                <Button
                                    style={{marginTop: '25px'}}
                                    type="primary" icon={<DownloadOutlined/>}
                                    onClick={selectAllRemote}>{t('SELECT_ALL_LABEL')}</Button>
                                <Button
                                    style={{marginTop: '25px'}}
                                    type="primary" icon={<DownloadOutlined/>}
                                    onClick={handleLoading}
                                    loading={isLoadingLoading}>{t('LOAD_SELECTED_LABEL')}</Button>
                            </Space>
                        </Col>
                    </Row>
                    }
                    <Row>
                        <Typography.Title level={2}>{t('LOCAL_STATIONS_LABEL')}</Typography.Title>
                    </Row>
                    <Row>
                        <DataTable
                            columns={localStationsColumns}
                            data={localStations}
                            updateSelectedData={setSelectedLocalStations}
                            pagination={tablePagination}
                            setPagination={setTablePagination}
                            isAnyLoading={isAnyLoading}
                            searchableColumns={searchableStationColumns}
                        />
                    </Row>
                    <Row>
                        <Space>
                            <Button
                                style={{marginTop:'25px'}}
                                type="primary"
                                size="large"
                                icon={<DownloadOutlined/>}
                                onClick={fetchLocalStations}
                                loading={isLocalStationsLoading}>
                                {t('FETCH_LOCAL_LABEL')}
                            </Button>
                            {
                                localStations.length > 0 &&
                                <>
                                    <Button
                                        style={{marginTop:'25px'}}
                                        type="primary"
                                        size="large"
                                        onClick={selectAllLocal}
                                    >
                                        {t('SELECT_ALL_LABEL')}
                                    </Button>
                                    <Button
                                        style={{marginTop:'25px'}}
                                        type="primary"
                                        size="large"
                                        loading={isDeletingLoading}
                                        onClick={deleteSelectedLocalStations}
                                    >
                                        {t('DELETE_SELECTED_LABEL')}
                                    </Button>
                                </>
                            }
                        </Space>
                    </Row>
                </Flex>
            </div>
        </>
    )
}
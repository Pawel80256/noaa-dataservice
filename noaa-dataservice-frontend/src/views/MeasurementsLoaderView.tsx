import {Button, Col, DatePicker, Flex, Input, Row, Space, TableProps, Typography} from "antd";
import {useTranslation} from "react-i18next";
import {StationsTable} from "../components/data_loader/stations/StationsTable";
import {useEffect, useState} from "react";
import {NOAAStation} from "../models/NOAAStation";
import {CheckCircleOutlined, CloseCircleOutlined, DownloadOutlined} from "@ant-design/icons";
import {getAllLocalStations} from "../services/NOAAStationService";
import {showErrorNotification, showSuccessNotification} from "../services/Utils";
import {MeasurementsTable} from "../components/data_loader/measurements/MeasurementsTable";
import {NOAAData} from "../models/NOAADataDto";
import {getRemoteMeasurements, loadMeasurements} from "../services/NOAADataService";
import {DataTable} from "../components/common/DataTable";

export const MeasurementsLoaderView = () => {
    const { t } = useTranslation();
    const [localStations, setLocalStations] = useState<NOAAStation[]>([]);
    const [selectedLocalStations, setSelectedLocalStations] = useState<React.Key[]>([]);
    // const [dateRange, setDateRange] = useState({ startDate: [""], endDate: [""] });
    const [startDate, setStartDate] = useState<string | string[]>("")
    const [endDate, setEndDate] = useState<string | string[]>("")
    const [dataset, setDataset] = useState('');

    const [isLocalStationsLoading, setIsLocalStationsLoading] = useState<boolean>(false);
    const [isRemoteMeasurementsLoading, setIsRemoteMeasurementsLoading] = useState<boolean>(false);
    const [isLoadingMeasurementsLoading, setIsLoadingMeasurementsLoading] = useState<boolean>(false);
    const [isAnyLoading,setIsAnyLoading] = useState<boolean>(false)

    const [remoteMeasurements, setRemoteMeasurements] = useState<NOAAData[]>([]);

    const [tablePagination, setTablePagination] = useState({current: 1, pageSize: 5});


    useEffect(() => {
        setIsAnyLoading(isLocalStationsLoading || isRemoteMeasurementsLoading || isLoadingMeasurementsLoading)
    }, []);
    const fetchLocalStations = () => {
        setIsLocalStationsLoading(true);
        getAllLocalStations().then(response => {
            setLocalStations(response);
            setIsLocalStationsLoading(false);
            showSuccessNotification(t('LOCAL_FETCH_SUCCESS_LABEL'));
        }).catch(() => {
            setIsLocalStationsLoading(false);
            showErrorNotification(t('LOCAL_FETCH_ERROR_LABEL'))
        })
    }

    const fetchRemoteMeasurements = () => {
        setIsRemoteMeasurementsLoading(true);
        getRemoteMeasurements(startDate,endDate, selectedLocalStations[0].toString()).then(response => {
            setRemoteMeasurements(response);
            setIsRemoteMeasurementsLoading(false);
            showSuccessNotification(t('REMOTE_FETCH_SUCCESS_LABEL'));
        }).catch(() => {
            setIsRemoteMeasurementsLoading(false);
            showErrorNotification(t('REMOTE_FETCH_ERROR_LABEL'));
        });
    };

    const loadAllRemoteMeasurements = () => {
        setIsLoadingMeasurementsLoading(true);
        loadMeasurements(startDate,endDate, selectedLocalStations[0].toString()).then(() => {
            fetchRemoteMeasurements()
            setIsLoadingMeasurementsLoading(false);
            showSuccessNotification(t('LOAD_SUCCESS_LABEL'))
        }).catch(()=>{
            setIsLoadingMeasurementsLoading(false);
            showErrorNotification(t('LOAD_ERROR_LABEL'))
        })
    }

    const stationColumns: TableProps<NOAAStation>['columns'] = [
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
            sorter: (a, b) => a.elevationUnit.localeCompare(b.elevationUnit)
        },
        {
            title: t('LATITUDE_COLUMN'),
            dataIndex: 'latitude',
            key: 'latitude',
            sorter: (a, b) => a.latitude - b.latitude,
        },
        {
            title: t('LONGITUDE_COLUMN'),
            dataIndex: 'longitude',
            key: 'longitude',
            sorter: (a, b) => a.longitude - b.longitude,
        },
    ]

    const searchableStationColumns = ["id","name","dataCoverage","minDate","maxDate","elevation","elevationUnit","latitude","longitude"]

    const measurementColumns: TableProps<NOAAData>['columns'] = [
        {
            title: t('INDEX_COLUMN'),
            key: 'index',
            render: (value, item, index) => (tablePagination.current - 1) * tablePagination.pageSize + index + 1
        },
        // {
        //     title: t('IDENTIFIER_COLUMN'),
        //     dataIndex:'id',
        //     key:'id'
        // },
        {
            title: t('DATA_TYPE_COLUMN'),
            dataIndex: 'dataTypeId',
            key: 'dataTypeId',
            sorter: (a, b) => a.datatype.localeCompare(b.datatype)
        },
        {
            title: t('DATE_COLUMN'),
            dataIndex: 'date',
            key: 'date',
            sorter: (a, b) => new Date(a.date).getTime() - new Date(b.date).getTime(),
        },
        {
            title: t('VALUE_COLUMN'),
            dataIndex: 'value',
            key: 'value',
            sorter: (a, b) => a.value - b.value,
        },
        {
            title: t('ATTRIBUTES_COLUMN'),
            dataIndex: 'attributes',
            key: 'attributes',
            sorter: (a, b) => a.attributes.localeCompare(b.attributes)
        },
        {
            title: t('STATION_COLUMN'),
            dataIndex: 'stationId',
            key: 'stationId',
            sorter: (a, b) => a.station.localeCompare(b.station)
        },
        {
            title: t('STATUS_COLUMN'),
            dataIndex: 'loaded',
            key: 'loaded',
            render: (value, item, index) => {
                if (value) {
                    return <CheckCircleOutlined style={{color: 'green'}}/>;
                } else {
                    return <CloseCircleOutlined style={{color: 'red'}}/>;
                }
            }
        },

    ]

    const searchableMeasurementColumns = ["dataTypeId","date","value","attributes","stationId","loaded"]
    return (
        <>
            <div style={{ display: 'flex', flexDirection: 'column', height: '100vh' }}>
                <Flex style={{ flex: 1, minWidth: '50vh' }} align={'center'} justify={'flex-start'} vertical>
                    <Row>
                        <Typography.Title level={2}>{t('LOCAL_STATIONS_LABEL')}</Typography.Title>
                    </Row>
                    <Row>
                        <DataTable
                            columns={stationColumns}
                            data={localStations}
                            updateSelectedData={setSelectedLocalStations}
                            pagination={tablePagination}
                            setPagination={setTablePagination}
                            singleSelect={true}
                            searchableColumns={searchableStationColumns}
                            isAnyLoading={isAnyLoading}
                            />
                    </Row>
                    <Row>
                        <Button
                            style={{ marginTop: '25px' }}
                            type="primary"
                            size="large"
                            icon={<DownloadOutlined />}
                            onClick={fetchLocalStations}
                            loading={isLocalStationsLoading}>
                            {t('FETCH_LOCAL_LABEL')}
                        </Button>
                    </Row>
                    {
                        localStations.length > 0 &&
                        <>
                            <Row style={{marginTop:"2%"}}>
                                <Space>
                                    <DatePicker onChange={(date, dateString) => setStartDate(dateString)} />
                                    <DatePicker onChange={(date, dateString) => setEndDate(dateString)} />
                                </Space>
                            </Row>
                            <Row>
                                <Button
                                    style={{ marginTop: '25px',marginBottom:"25px" }}
                                    type="primary"
                                    size="large"
                                    onClick={fetchRemoteMeasurements}
                                    loading = {isRemoteMeasurementsLoading}
                                    disabled={!startDate || !endDate || selectedLocalStations.length == 0}
                                >
                                    {t('SEARCH_MEASUREMENTS_LABEL')}
                                </Button>
                            </Row>
                            <Row>
                                <DataTable
                                    columns={measurementColumns}
                                    data={remoteMeasurements}
                                    pagination={tablePagination}
                                    setPagination={setTablePagination}
                                    searchableColumns={searchableMeasurementColumns}
                                    isAnyLoading={isAnyLoading}
                                    />
                            </Row>
                            <Row>
                                <Col>
                                    <Space>
                                        <Button
                                            style={{marginTop:'25px', marginBottom:'30px'}}
                                            type="primary" icon={<DownloadOutlined />}
                                            onClick={loadAllRemoteMeasurements}
                                            loading={isLoadingMeasurementsLoading}>{t('LOAD_ALL_LABEL')}
                                        </Button>
                                    </Space>
                                </Col>
                            </Row>
                        </>
                    }

                </Flex>
            </div>
        </>
    )
}

import { Button, Flex, Row, Space, Typography, DatePicker, Input } from "antd";
import { useTranslation } from "react-i18next";
import { StationsTable } from "../components/data_loader/stations/StationsTable";
import { useState } from "react";
import { NOAAStation } from "../models/NOAAStation";
import { DownloadOutlined } from "@ant-design/icons";
import { getAllLocalStations } from "../services/NOAAStationService";
import { showErrorNotification, showSuccessNotification } from "../services/Utils";
import {MeasurementsTable} from "../components/data_loader/measurements/MeasurementsTable";
import {NOAAData} from "../models/NOAAData";
import {getAllRemoteCountries} from "../services/NOAALocationService";
import {getRemoteMeasurements} from "../services/NOAADataService";

export const MeasurementsLoaderView = () => {
    const { t } = useTranslation();
    const [localStations, setLocalStations] = useState<NOAAStation[]>([]);
    const [selectedLocalStations, setSelectedLocalStations] = useState<React.Key[]>([]);
    const [isLocalStationsLoading, setIsLocalStationsLoading] = useState<boolean>(false);
    // const [dateRange, setDateRange] = useState({ startDate: [""], endDate: [""] });
    const [startDate, setStartDate] = useState<string | string[]>("")
    const [endDate, setEndDate] = useState<string | string[]>("")
    const [dataset, setDataset] = useState('');

    const [remoteMeasurements, setRemoteMeasurements] = useState<NOAAData[]>([]);
    const [isRemoteMeasurementsLoading, setIsRemoteMeasurementsLoading] = useState<boolean>(false);
    const updateSelectedLocalStations = (keys: React.Key[]) => {
        setSelectedLocalStations(keys)
    }

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

    return (
        <>
            <div style={{ display: 'flex', flexDirection: 'column', height: '100vh' }}>
                <Flex style={{ flex: 1, minWidth: '50vh' }} align={'center'} justify={'flex-start'} vertical>
                    <Row>
                        <Typography.Title level={2}>{t('LOCAL_STATIONS_LABEL')}</Typography.Title>
                    </Row>
                    <Row>
                        <StationsTable
                            stations={localStations}
                            updateSelectedLocations={updateSelectedLocalStations}
                            localStations={localStations}
                            selectedStations={selectedLocalStations}
                            multiSelect={false} />
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
                                    <Input placeholder="Dataset" onChange={e => setDataset(e.target.value)} />
                                </Space>
                            </Row>
                            <Row>
                                <Button
                                    style={{ marginTop: '25px' }}
                                    type="primary"
                                    size="large"
                                    onClick={fetchRemoteMeasurements}
                                    loading = {isRemoteMeasurementsLoading}
                                >
                                    {t('SEARCH_MEASUREMENTS_LABEL')}
                                </Button>
                            </Row>
                            <Row>
                                <MeasurementsTable measurements={remoteMeasurements}/>
                            </Row>
                        </>
                    }

                </Flex>
            </div>
        </>
    )
}

import { Button, Flex, Row, Space, Typography, DatePicker, Input } from "antd";
import { useTranslation } from "react-i18next";
import { StationsTable } from "../components/data_loader/stations/StationsTable";
import { useState } from "react";
import { NOAAStation } from "../models/NOAAStation";
import { DownloadOutlined } from "@ant-design/icons";
import { getAllLocalStations } from "../services/NOAAStationService";
import { showErrorNotification, showSuccessNotification } from "../services/Utils";

export const MeasurementsLoaderView = () => {
    const { t } = useTranslation();
    const [localStations, setLocalStations] = useState<NOAAStation[]>([]);
    const [selectedLocalStations, setSelectedLocalStations] = useState<React.Key[]>([]);
    const [isLocalStationsLoading, setIsLocalStationsLoading] = useState<boolean>(false);
    // const [dateRange, setDateRange] = useState({ startDate: [""], endDate: [""] });
    const [startDate, setStartDate] = useState<string | string[]>("")
    const [endDate, setEndDate] = useState<string | string[]>("")
    const [dataset, setDataset] = useState('');

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

    const handleSubmit = () => {
        console.log(startDate);
        console.log(endDate);
        console.log('Dataset:', dataset);
    }

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
                    <Row style={{marginTop:"2%"}}>
                        <Space>
                            <DatePicker onChange={(date, dateString) => setStartDate(dateString)} />
                            <DatePicker onChange={(date, dateString) => setEndDate(dateString)} />
                            <Input placeholder="Dataset" onChange={e => setDataset(e.target.value)} />
                        </Space>
                    </Row>
                    <Row>
                        <Space>
                            <Button
                                style={{ marginTop: '25px' }}
                                type="primary"
                                size="large"
                                onClick={handleSubmit}>
                                Wypisz wartości
                            </Button>
                            <Button
                                style={{ marginTop: '25px' }}
                                type="primary"
                                size="large"
                                icon={<DownloadOutlined />}
                                onClick={fetchLocalStations}
                                loading={isLocalStationsLoading}>
                                {t('FETCH_LOCAL_LABEL')}
                            </Button>
                        </Space>
                    </Row>
                </Flex>
            </div>
        </>
    )
}

import {Button, Col, Divider, Flex, Row, Space, Typography} from "antd";
import {useTranslation} from "react-i18next";
import {BankOutlined, DownloadOutlined, GlobalOutlined, TableOutlined} from "@ant-design/icons";
import {useState} from "react";
import {NOAALocation} from "../models/NOAALocation";
import {LocationsTable} from "../components/data_loader/locations/LocationsTable";
import {getAllLocalCities, getAllLocalCountries, getAllLocalStates} from "../services/NOAALocationService";
import {showErrorNotification, showSuccessNotification} from "../services/Utils";
import {NOAAStation} from "../models/NOAAStation";
import {getRemoteStationsByLocationId} from "../services/NOAAStationService";
import Title from "antd/es/skeleton/Title";
import {StationsTable} from "../components/data_loader/stations/StationsTable";

export const StationsLoaderView = () => {
    //wybieranie kategorii lokalizacji do szukania (kraje / miasta / stany)
    const {t} = useTranslation();
    const [localLocations, setLocalLocations] = useState<NOAALocation[]>([]);

    const [localStations, setLocalStations] = useState<NOAAStation[]>([]);
    const [remoteStations, setRemoteStations] = useState<NOAAStation[]>([]);

    const [selectedLocation, setSelectedLocation] = useState<React.Key | null>(null);
    const [selectedLocalStations, setSelectedLocalStations] = useState<React.Key[]>([]);
    const [selectedRemoteStations, setSelectedRemoteStations] = useState<React.Key[]>([]);

    const [isRemoteStationsLoading, setIsRemoteStationsLoading] = useState<boolean>(false);

    const updateSelectedRemoteStations = (keys: React.Key[]) => {
        setSelectedRemoteStations(keys)
    }
    const updateSelectedLocalStations = (keys: React.Key[]) => {
        setSelectedLocalStations(keys)
    }

    const fetchRemoteStationsByLocationId = (locationId:string) => {
            setIsRemoteStationsLoading(true);
            getRemoteStationsByLocationId(locationId).then(response =>{
                setRemoteStations(response);
                setIsRemoteStationsLoading(false);
                showSuccessNotification(t('REMOTE_FETCH_SUCCESS_LABEL'));
            }).catch(() => {
                setIsRemoteStationsLoading(false);
                showErrorNotification(t('REMOTE_FETCH_ERROR_LABEL'));
            });
    }

    const fetchLocalLocations = (locationCategory:string) => {
        switch (locationCategory){

            case "CNTRY":{
                getAllLocalCountries().then(res => {
                    setLocalLocations(res);
                    showSuccessNotification(t('LOCAL_FETCH_SUCCESS_LABEL'));
                }).catch(()=>{
                    showErrorNotification(t('LOCAL_FETCH_ERROR_LABEL'));
                });
                break;
            }

            case "CITY":{
                getAllLocalCities().then(res => {
                    setLocalLocations(res);
                    showSuccessNotification(t('LOCAL_FETCH_SUCCESS_LABEL'));
                }).catch(()=>{
                    showErrorNotification(t('LOCAL_FETCH_ERROR_LABEL'));
                });
                break;
            }

            case "ST":{
                getAllLocalStates().then(res => {
                    setLocalLocations(res);
                    showSuccessNotification(t('LOCAL_FETCH_SUCCESS_LABEL'));
                }).catch(()=>{
                    showErrorNotification(t('LOCAL_FETCH_ERROR_LABEL'));
                });
                break;
            }

            default:{
                console.log("Incorrect Location Category");
            }
        }
    }

    return (
        <>
            <div style={{display: 'flex', flexDirection: 'column', height: '100vh'}}>
                <Flex style={{flex: 1, minWidth: '50vh'}} align={'center'} justify={'flex-start'} vertical>
                    <Row style={{marginTop:"5%"}}>
                        <Col>
                            <Space>
                                <Button
                                    type="primary"
                                    size="large"
                                    onClick={()=>fetchLocalLocations("CNTRY")}
                                    icon = {<GlobalOutlined/>}>{t('SEARCH_COUNTRIES_LABEL')}</Button>
                                <Button
                                    type="primary"
                                    size="large"
                                    onClick={()=>fetchLocalLocations("CITY")}
                                    icon = {<BankOutlined/>}>{t('SEARCH_CITIES_LABEL')}</Button>
                                <Button
                                    type="primary"
                                    size="large"
                                    onClick={()=>fetchLocalLocations("ST")}
                                    icon = {<TableOutlined/>}>{t('SEARCH_STATES_LABEL')}</Button>
                            </Space>
                        </Col>
                    </Row>
                    <Row style={{marginTop:"5%"}}>
                        <LocationsTable
                            locations={localLocations}
                            multiSelect={false}
                            updateSelectedLocations={(keys) => {
                                setSelectedLocation(keys.length > 0 ? keys[0] : null);
                            }}
                        />
                    </Row>
                    {localLocations.length >0 &&
                        <Row>
                            <Button
                                type="primary"
                                size="large"
                                style={{marginTop: '25px'}}
                                disabled={!selectedLocation}
                                onClick={() => {
                                    if (selectedLocation) {
                                        fetchRemoteStationsByLocationId(selectedLocation as string);
                                    }
                                }}
                                icon = {<DownloadOutlined/>}>{t('FIND_STATIONS_LABEL')}</Button>
                        </Row>
                    }
                    <Divider style={{marginTop:'50px'}}/>
                    <Row>
                        <Typography.Title level={2}>{t('REMOTE_STATIONS_LABEL')}</Typography.Title>
                    </Row>
                    <Row>
                        <StationsTable stations={remoteStations} updateSelectedLocations={updateSelectedRemoteStations} localStations={localStations} selectedStations={selectedRemoteStations}/>
                    </Row>
                </Flex>
            </div>
        </>
    )
}
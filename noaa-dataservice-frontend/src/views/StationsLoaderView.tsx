import {Button, Col, Flex, Row, Space} from "antd";
import {useTranslation} from "react-i18next";
import {BankOutlined, GlobalOutlined, TableOutlined} from "@ant-design/icons";
import {useState} from "react";
import {NOAALocation} from "../models/NOAALocation";
import {LocationsTable} from "../components/data_loader/locations/LocationsTable";
import {getAllLocalCities, getAllLocalCountries, getAllLocalStates} from "../services/NOAALocationService";
import {showErrorNotification, showSuccessNotification} from "../services/Utils";

export const StationsLoaderView = () => {
    //wybieranie kategorii lokalizacji do szukania (kraje / miasta / stany)
    const {t} = useTranslation();
    const [remoteLocations, setRemoteLocations] = useState<NOAALocation[]>([]);
    const [localLocations, setLocalLocations] = useState<NOAALocation[]>([]);

    const fetchLocalLocations = (locationCategory:string) => {
        var response;
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
                        <LocationsTable locations={localLocations}/>
                    </Row>
                </Flex>
            </div>
        </>
    )
}
import {Button, Col, Flex, Row, Space} from "antd";
import {useTranslation} from "react-i18next";
import {BankOutlined, GlobalOutlined, TableOutlined} from "@ant-design/icons";
import {useState} from "react";
import {NOAALocation} from "../models/NOAALocation";
import {LocationsTable} from "../components/data_loader/locations/LocationsTable";

export const StationsLoaderView = () => {
    //wybieranie kategorii lokalizacji do szukania (kraje / miasta / stany)
    const {t} = useTranslation();
    const [remoteLocations, setRemoteLocations] = useState<NOAALocation[]>([]);
    const [localLocations, setLocalLocations] = useState<NOAALocation[]>([]);

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
                                    icon = {<GlobalOutlined/>}>{t('SEARCH_COUNTRIES_LABEL')}</Button>
                                <Button
                                    type="primary"
                                    size="large"
                                    icon = {<BankOutlined/>}>{t('SEARCH_CITIES_LABEL')}</Button>
                                <Button
                                    type="primary"
                                    size="large"
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
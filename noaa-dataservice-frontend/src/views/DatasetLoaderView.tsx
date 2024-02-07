import {Divider, Flex, Row, Typography} from "antd";
import {useTranslation} from "react-i18next";
import {RemoteDatasetsTable} from "../components/data_loader/RemoteDatasetsTable";

export const DatasetLoaderView = () =>{
    const {t} = useTranslation();
    return(
        <>
            <Flex style={{width:'100%', height:'50%'}} align={'center'} justify={'flex-start'} vertical>
                <Row>
                    <Typography.Title level={2}>{t('REMOTE_CONTENT')}</Typography.Title>
                </Row>
                <Row>
                    <RemoteDatasetsTable/>
                </Row>
            </Flex>
            <Flex style={{width:'100%', height:'50%'}} align={'flex-start'} justify={'center'}>
                <Typography.Title level={2}>{t('DATABASE_CONTENT')}</Typography.Title>
            </Flex>
        </>
    )
}
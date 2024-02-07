import {Button, Divider, Flex, Row, Typography} from "antd";
import {useTranslation} from "react-i18next";
import {RemoteDatasetsTable} from "../components/data_loader/RemoteDatasetsTable";
import {getAllDatasets} from "../services/NOAADatasetService";
import {useState} from "react";
import {NOAADataset} from "../models/NOAADataset";
import {initialPaginationWrapper, PaginationWrapper} from "../models/PaginationWrapper";
import { DownloadOutlined } from '@ant-design/icons';


export const DatasetLoaderView = () =>{
    const {t} = useTranslation();
    const [datasets, setDatasets] = useState<PaginationWrapper<NOAADataset>>(initialPaginationWrapper);
    const [isLoading, setIsLoading] = useState(false);

    const onClickTest = () => {
        setIsLoading(true);
        getAllDatasets(100, 1).then(response => {
            setDatasets(response);
            setIsLoading(false);
        }).catch(() => {
            setIsLoading(false);
        });
    };

    return(
        <>
            <Flex style={{width:'100%', height:'50%'}} align={'center'} justify={'flex-start'} vertical>
                <Row>
                    <Typography.Title level={2}>{t('REMOTE_CONTENT')}</Typography.Title>
                </Row>
                <Row>
                    <RemoteDatasetsTable datasets={datasets}/>
                </Row>
                {datasets.count === 0 &&    <Row>
                    <Button style={{marginTop:'25px'}} type="primary" icon={<DownloadOutlined />} onClick={onClickTest} loading={isLoading}>{t('FETCH_DATASETS_LABEL')}</Button>
                </Row>}
            </Flex>
            <Flex style={{width:'100%', height:'50%'}} align={'flex-start'} justify={'center'}>
                <Typography.Title level={2}>{t('DATABASE_CONTENT')}</Typography.Title>
            </Flex>
        </>
    )
}
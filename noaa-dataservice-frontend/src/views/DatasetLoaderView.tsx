import {Button, Divider, Flex, Row, Typography} from "antd";
import {useTranslation} from "react-i18next";
import {RemoteDatasetsTable} from "../components/data_loader/datasets/RemoteDatasetsTable";
import {getAllLocalDatasets, getAllRemoteDatasets} from "../services/NOAADatasetService";
import {useState} from "react";
import {NOAADataset} from "../models/NOAADataset";
import {initialPaginationWrapper, PaginationWrapper} from "../models/PaginationWrapper";
import { DownloadOutlined } from '@ant-design/icons';


export const DatasetLoaderView = () =>{
    const {t} = useTranslation();
    const [remoteDatasets, setRemoteDatasets] = useState<PaginationWrapper<NOAADataset>>(initialPaginationWrapper);
    const [localDatasets, setLocalDatasets] = useState<NOAADataset[]>([]);
    const [isRemoteDatasetsLoading, setIsRemoteDatasetsLoading] = useState(false);
    const [isLocalDatasetsLoading, setIsLocalDatasetsLoading] = useState(false);

    const fetchRemoteDatasets = () => {
        setIsRemoteDatasetsLoading(true);
        getAllRemoteDatasets(100, 1).then(response => {
            setRemoteDatasets(response);
            setIsRemoteDatasetsLoading(false);
        }).catch(() => {
            setIsRemoteDatasetsLoading(false);
        });
    };

    const fetchLocalDatasets = () => {
        setIsLocalDatasetsLoading(true);
        getAllLocalDatasets().then(response => {
            setLocalDatasets(response);
            setIsLocalDatasetsLoading(false);
        }).catch(()=>{
            setIsLocalDatasetsLoading(false);
        })
    }

    return(
        <>
            <Flex style={{width:'100%', height:'50%'}} align={'center'} justify={'flex-start'} vertical>
                <Row>
                    <Typography.Title level={2}>{t('REMOTE_CONTENT')}</Typography.Title>
                </Row>
                <Row>
                    <RemoteDatasetsTable datasets={remoteDatasets.data}/>
                </Row>
                {remoteDatasets.count === 0 &&    <Row>
                    <Button style={{marginTop:'25px'}} type="primary" icon={<DownloadOutlined />} onClick={fetchRemoteDatasets} loading={isRemoteDatasetsLoading}>{t('FETCH_REMOTE_LABEL')}</Button>
                </Row>}
            </Flex>
            <Flex style={{width:'100%', height:'50%'}} align={'center'} justify={'flex-start'} vertical>
                <Row>
                    <Typography.Title level={2}>{t('DATABASE_CONTENT')}</Typography.Title>
                </Row>
                <Row>
                    <RemoteDatasetsTable datasets={localDatasets}/>
                </Row>
                {localDatasets.length === 0 &&    <Row>
                    <Button style={{marginTop:'25px'}} type="primary" icon={<DownloadOutlined />} onClick={fetchLocalDatasets} loading={isRemoteDatasetsLoading}>{t('FETCH_LOCAL_LABEL')}</Button>
                </Row>}
            </Flex>
        </>
    )
}
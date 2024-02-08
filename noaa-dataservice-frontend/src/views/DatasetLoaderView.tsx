import {Button, Divider, Flex, Row, Typography} from "antd";
import {useTranslation} from "react-i18next";
import {DatasetsTable} from "../components/data_loader/datasets/DatasetsTable";
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
    const [isLocalDatasetsLoading, setIsLocalDatasetsLoading] = useState(false)

    const [selectedRemoteDatasets, setSelectedRemoteDatasets] = useState<React.Key[]>([]);

    const updateSelectedRemoteDatasets = (keys: React.Key[]) => {
        setSelectedRemoteDatasets(keys)
        // console.log(selectedRemoteDatasets)
    }

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
                    <DatasetsTable datasets={remoteDatasets.data} updateSelectedRemoteDatasets={updateSelectedRemoteDatasets}/>
                </Row>
                {remoteDatasets.count === 0 &&    <Row>
                    <Button style={{marginTop:'25px'}} type="primary" icon={<DownloadOutlined />} onClick={fetchRemoteDatasets} loading={isRemoteDatasetsLoading}>{t('FETCH_REMOTE_LABEL')}</Button>
                </Row>}
                {remoteDatasets.count > 0 &&    <Row>
                    <Button style={{marginTop:'25px'}} type="primary" icon={<DownloadOutlined />} onClick={() => console.log(selectedRemoteDatasets)} loading={isRemoteDatasetsLoading}>Show selected</Button>
                </Row>}
            </Flex>
            //chcialbym zeby ponizszy element dopasowywal sie do wysokosci poprzedniego, tzn ten na gorze moze sie zwiekszac/zmniejszac zaleznie od wielkosci tabeli, ponizej pewnej wielkosci gornej tabeli dolny komponent ma byc w polowie wysokosci ekranu, jezeli powyzszy komponent spuchnie, to ponizszy ma sie przesunac zachowujac odpowiedni odstęp
            <Flex style={{width:'100%', height:'50%'}} align={'center'} justify={'flex-start'} vertical>
                <Row>
                    <Typography.Title level={2}>{t('DATABASE_CONTENT')}</Typography.Title>
                </Row>
                <Row>
                    <DatasetsTable datasets={localDatasets} updateSelectedRemoteDatasets={updateSelectedRemoteDatasets}/>
                </Row>
                {localDatasets.length === 0 &&    <Row>
                    <Button style={{marginTop:'25px'}} type="primary" icon={<DownloadOutlined />} onClick={fetchLocalDatasets} loading={isLocalDatasetsLoading}>{t('FETCH_LOCAL_LABEL')}</Button>
                </Row>}
            </Flex>
        </>
    )
}
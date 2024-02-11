import {Button, Flex, notification, Row, Typography} from "antd";
import {useTranslation} from "react-i18next";
import {DatasetsTable} from "../components/data_loader/datasets/DatasetsTable";
import {
    deleteDatasetsByIds,
    getAllLocalDatasets,
    getAllRemoteDatasets,
    loadByIds
} from "../services/NOAADatasetService";
import {useState} from "react";
import {NOAADataset} from "../models/NOAADataset";
import {initialPaginationWrapper, PaginationWrapper} from "../models/PaginationWrapper";
import {DownloadOutlined} from '@ant-design/icons';
import {showSuccessNotification} from "../services/Utils";


export const DatasetLoaderView = () =>{
    const {t} = useTranslation();
    const [api, contextHolder] = notification.useNotification();

    const [remoteDatasets, setRemoteDatasets] = useState<PaginationWrapper<NOAADataset>>(initialPaginationWrapper);
    const [localDatasets, setLocalDatasets] = useState<NOAADataset[]>([]);

    const [isRemoteDatasetsLoading, setIsRemoteDatasetsLoading] = useState(false);
    const [isLocalDatasetsLoading, setIsLocalDatasetsLoading] = useState(false)
    const [isLoadingDatasetsLoading, setIsLoadingDatasetsLoading] = useState(false)
    const [isDeletingDatasetsLoading, setIsDeletingDatasetsLoading] = useState(false)

    const [selectedRemoteDatasets, setSelectedRemoteDatasets] = useState<React.Key[]>([]);
    const [selectedLocalDatasets, setSelectedLocalDatasets] = useState<React.Key[]>([]);

    const updateSelectedRemoteDatasets = (keys: React.Key[]) => {
        setSelectedRemoteDatasets(keys)
    }
    const updateSelectedLocalDatasets = (keys: React.Key[]) => {
        setSelectedLocalDatasets(keys)
    }

    const fetchRemoteDatasets = () => {
        setIsRemoteDatasetsLoading(true);
        getAllRemoteDatasets(100, 1).then(response => {
            setRemoteDatasets(response);
            setIsRemoteDatasetsLoading(false);
            showSuccessNotification(t('REMOTE_FETCH_SUCCESS_LABEL'));
        }).catch(() => {
            setIsRemoteDatasetsLoading(false);
        });
    };

    const fetchLocalDatasets = () => {
        setIsLocalDatasetsLoading(true);
        getAllLocalDatasets().then(response => {
            setLocalDatasets(response);
            setIsLocalDatasetsLoading(false);
            showSuccessNotification(t('LOCAL_FETCH_SUCCESS_LABEL'))
        }).catch(()=>{
            setIsLocalDatasetsLoading(false);
        })
    }

    const loadSelectedDatasets = () => {
        const ids:string[] = selectedRemoteDatasets.map(key => key.toString());
        setIsLoadingDatasetsLoading(true);
        loadByIds(ids).then(() => {
            fetchLocalDatasets();
            setIsLoadingDatasetsLoading(false);
            showSuccessNotification(t('LOAD_SUCCESS_LABEL'))
        }).catch(()=>{
            setIsLoadingDatasetsLoading(false);
        })
    }

    const deleteSelectedDatasets = () => {
        const ids:string[] = selectedLocalDatasets.map(key => key.toString());
        setIsDeletingDatasetsLoading(true);
        deleteDatasetsByIds(ids).then(() => {
            fetchLocalDatasets();
            setIsDeletingDatasetsLoading(false);
            showSuccessNotification(t('DELETE_SUCCESS_LABEL'))
        }).catch(()=>{
            setIsDeletingDatasetsLoading(false);
        })
    }


    return(
        <>
            {contextHolder}
            <div style={{ display: 'flex', flexDirection: 'column', height: '100vh' }}>
                <Flex style={{ flex: 1, minWidth: '50vh' }} align={'center'} justify={'flex-start'} vertical>
                    <Row>
                        <Typography.Title level={2}>{t('REMOTE_CONTENT')}</Typography.Title>
                    </Row>
                    <Row>
                        <DatasetsTable
                            datasets={remoteDatasets.data}
                            updateSelectedDatasets={updateSelectedRemoteDatasets}
                            localDatasets={localDatasets}
                            showStatusColumn={true}/>
                    </Row>
                    {remoteDatasets.count === 0 &&    <Row>
                        <Button
                            style={{marginTop:'25px'}}
                            type="primary" icon={<DownloadOutlined />}
                            onClick={fetchRemoteDatasets}
                            loading={isRemoteDatasetsLoading}>{t('FETCH_REMOTE_LABEL')}</Button>
                    </Row>}
                    {remoteDatasets.count > 0 &&    <Row>
                        <Button
                            style={{marginTop:'25px'}}
                            type="primary" icon={<DownloadOutlined />}
                            onClick={loadSelectedDatasets}
                            loading={isLoadingDatasetsLoading}>{t('LOAD_SELECTED_LABEL')}</Button>
                    </Row>}
                </Flex>
                <Flex style={{ flex: 1, minHeight: '50vh' }} align={'center'} justify={'flex-start'} vertical>
                    <Row>
                        <Typography.Title level={2}>{t('DATABASE_CONTENT')}</Typography.Title>
                    </Row>
                    <Row>
                        <DatasetsTable
                            datasets={localDatasets}
                            updateSelectedDatasets={updateSelectedLocalDatasets}
                            localDatasets={[]}
                            showStatusColumn={false}/>
                    </Row>
                    {localDatasets.length === 0 &&    <Row>
                        <Button
                            style={{marginTop:'25px'}}
                            type="primary" icon={<DownloadOutlined />}
                            onClick={fetchLocalDatasets}
                            loading={isLocalDatasetsLoading}>{t('FETCH_LOCAL_LABEL')}</Button>
                    </Row>}
                    {localDatasets.length > 0 &&    <Row>
                        <Button
                            style={{marginTop:'25px'}}
                            type="primary" icon={<DownloadOutlined />}
                            onClick={deleteSelectedDatasets}
                            loading={isDeletingDatasetsLoading}>{t('DELETE_SELECTED_LABEL')}</Button>
                    </Row>}
                </Flex>
            </div>

        </>
    )
}
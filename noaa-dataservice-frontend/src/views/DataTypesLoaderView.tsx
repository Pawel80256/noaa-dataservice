import {useTranslation} from "react-i18next";
import {Button, Col, Flex, notification, Row, Space, Typography} from "antd";
import {useState} from "react";
import {NOAADataType} from "../models/NOAADataType";
import {showErrorNotification, showSuccessNotification} from "../services/Utils";
import {deleteLocalDataTypesByIds, getAllLocalDataTypes, getAllRemoteDataTypes} from "../services/NOAADataTypeService";
import {DownloadOutlined} from "@ant-design/icons";
import {DataTypesTable} from "../components/data_loader/data_types/DataTypesTable";
import Column from "antd/es/table/Column";

export const DataTypesLoaderView = () => {
    const {t} = useTranslation();
    const [api, contextHolder] = notification.useNotification();

    const [remoteDataTypes, setRemoteDataTypes] = useState<NOAADataType[]>([]);
    const [localDataTypes, setLocalDataTypes] = useState<NOAADataType[]>([]);

    const [isRemoteDataTypesLoading, setIsRemoteDataTypesLoading] = useState(false);
    const [isLocalDataTypesLoading, setIsLocalDataTypesLoading] = useState(false)
    const [isLoadingDataTypesLoading, setIsLoadingDataTypesLoading] = useState(false)
    const [isDeletingDataTypesLoading, setIsDeletingDataTypesLoading] = useState(false)

    const [selectedRemoteDataTypes, setSelectedRemoteDataTypes] = useState<React.Key[]>([]);
    const [selectedLocalDataTypes, setSelectedLocalDataTypes] = useState<React.Key[]>([]);

    const updateSelectedRemoteDataTypes = (keys: React.Key[]) => {
        setSelectedRemoteDataTypes(keys)
    }
    const updateSelectedLocalDataTypes = (keys: React.Key[]) => {
        setSelectedLocalDataTypes(keys)
    }

    const selectAllLocalDataTypes = () => {
        const newSelectedRowKeys = localDataTypes.map(dt => dt.id);
        setSelectedLocalDataTypes(newSelectedRowKeys);
    };

    const fetchRemoteDataTypes = () => {
        setIsRemoteDataTypesLoading(true);
        getAllRemoteDataTypes().then(response => {
            setRemoteDataTypes(response);
            setIsRemoteDataTypesLoading(false);
            showSuccessNotification(t('REMOTE_FETCH_SUCCESS_LABEL'));
        }).catch(() => {
            setIsRemoteDataTypesLoading(false);
            showErrorNotification(t('REMOTE_FETCH_ERROR_LABEL'));
        });
    };

    const fetchLocalDataTypes = () => {
        setIsLocalDataTypesLoading(true);
        getAllLocalDataTypes().then(response => {
            setLocalDataTypes(response);
            setIsLocalDataTypesLoading(false);
            showSuccessNotification(t('LOCAL_FETCH_SUCCESS_LABEL'))
        }).catch(()=>{
            setIsLocalDataTypesLoading(false);
            showErrorNotification(t('LOCAL_FETCH_ERROR_LABEL'))
        })
    }

    const deleteSelectedDataTypes = () => {
        const ids:string[] = selectedLocalDataTypes.map(key => key.toString());
        setIsDeletingDataTypesLoading(true);
        deleteLocalDataTypesByIds(ids).then(() => {
            fetchLocalDataTypes(/*boolean showNotification*/);
            setIsDeletingDataTypesLoading(false);
            showSuccessNotification(t('DELETE_SUCCESS_LABEL'))
        }).catch(()=>{
            setIsDeletingDataTypesLoading(false);
            showErrorNotification(t('DELETE_ERROR_LABEL'))
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
                        <DataTypesTable
                            dataTypes={remoteDataTypes}
                            updateSelectedDataTypes={updateSelectedRemoteDataTypes}
                            localDataTypes={localDataTypes}
                            selectedDataTypes={selectedRemoteDataTypes}
                            showStatusColumn={true}/>
                    </Row>
                    {remoteDataTypes.length === 0 &&    <Row>
                        <Button
                            style={{marginTop:'25px'}}
                            type="primary" icon={<DownloadOutlined />}
                            onClick={fetchRemoteDataTypes}
                            loading={isRemoteDataTypesLoading}>{t('FETCH_REMOTE_LABEL')}</Button>
                    </Row>}
                    {remoteDataTypes.length > 0 &&    <Row>
                        <Button
                            style={{marginTop:'25px'}}
                            type="primary" icon={<DownloadOutlined />}
                            onClick={() =>{}}
                            loading={isLoadingDataTypesLoading}>{t('LOAD_SELECTED_LABEL')}</Button>
                    </Row>}
                </Flex>
                <Flex style={{ flex: 1, minHeight: '50vh' }} align={'center'} justify={'flex-start'} vertical>
                    <Row>
                        <Typography.Title level={2}>{t('DATABASE_CONTENT')}</Typography.Title>
                    </Row>
                    <Row>
                        <DataTypesTable
                            dataTypes={localDataTypes}
                            updateSelectedDataTypes={updateSelectedLocalDataTypes}
                            localDataTypes={[]}
                            selectedDataTypes={selectedLocalDataTypes}
                            showStatusColumn={false}/>
                    </Row>
                    {localDataTypes.length === 0 &&    <Row>
                        <Button
                            style={{marginTop:'25px'}}
                            type="primary" icon={<DownloadOutlined />}
                            onClick={fetchLocalDataTypes}
                            loading={isLocalDataTypesLoading}>{t('FETCH_LOCAL_LABEL')}</Button>
                    </Row>}
                    {localDataTypes.length > 0 &&    <Row>
                        <Col>
                            <Space>
                                <Button
                                    style={{marginTop:'25px'}}
                                    type="primary" icon={<DownloadOutlined />}
                                    onClick={selectAllLocalDataTypes}
                                    loading={isDeletingDataTypesLoading}>Select all (translate)</Button>
                                <Button
                                    style={{marginTop:'25px'}}
                                    type="primary" icon={<DownloadOutlined />}
                                    onClick={deleteSelectedDataTypes}
                                    loading={isDeletingDataTypesLoading}>{t('DELETE_SELECTED_LABEL')}</Button>
                            </Space>
                        </Col>
                    </Row>}
                </Flex>
            </div>

        </>
    )
}
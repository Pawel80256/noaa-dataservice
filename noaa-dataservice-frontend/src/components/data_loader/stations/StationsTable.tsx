import {NOAAStation} from "../../../models/NOAAStation";
import {useTranslation} from "react-i18next";
import {useEffect, useState} from "react";
import {Table, TableProps} from "antd";
import {NOAADataType} from "../../../models/NOAADataType";
import {CheckCircleOutlined, CloseCircleOutlined, QuestionCircleOutlined} from "@ant-design/icons";

export interface StationsTableProps {
    stations:NOAAStation[],
    updateSelectedLocations: (keys: React.Key[]) => void,
    localStations: NOAAStation[],
    selectedStations: React.Key[],
    showStatusColumn?: boolean
}

export const StationsTable = (
    {stations,updateSelectedLocations,localStations,selectedStations,showStatusColumn}:StationsTableProps
) => {
    const {t} = useTranslation();
    const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]);
    const [pagination, setPagination] = useState<{current:number,pageSize:number}>({ current: 1, pageSize: 5 });

    useEffect(()=>{
        setSelectedRowKeys(selectedStations)
    }, [selectedStations])

    const handleTableChange = (pagination:any) => {
        setPagination({
            current: pagination.current,
            pageSize: pagination.pageSize
        });
    };

    const onSelectChange = (newSelectedRowKeys: React.Key[]) => {
        setSelectedRowKeys(newSelectedRowKeys);
        updateSelectedLocations(newSelectedRowKeys)
    };


    const rowSelection = {
        selectedRowKeys,
        onChange: onSelectChange,
    };

    const columns: TableProps<NOAAStation>['columns'] = [
        {
            title: t('INDEX_COLUMN'),
            key: 'index',
            render: (value, item, index) => (pagination.current - 1) * pagination.pageSize + index + 1
        },
        {
            title: t('IDENTIFIER_COLUMN'),
            dataIndex:'id',
            key:'id'
        },
        {
            title: t('NAME_COLUMN'),
            dataIndex:'name',
            key:'name'
        },
        {
            title: t('DATA_COVERAGE_COLUMN'),
            dataIndex:'datacoverage',
            key:'datacoverage',
        },
        {
            title: t('MIN_DATE_COLUMN'),
            dataIndex:'mindate',
            key:'mindate',
            render: (text, record) => record && record.mindate ? new Date(record.mindate).toISOString().split('T')[0] : ''
        },
        {
            title: t('MAX_DATE_COLUMN'),
            dataIndex:'maxdate',
            key:'maxdate',
            render: (text, record) => record && record.maxdate ? new Date(record.maxdate).toISOString().split('T')[0] : ''
        },
        {
            title: t('ELEVATION_COLUMN'),
            dataIndex: 'elevation',
            key: 'elevation'
        },
        {
            title: t('ELEVATION_UNIT_COLUMN'),
            dataIndex: 'elevationunit',
            key: 'elevationunit'
        },
        {
            title: t('LATITUDE_COLUMN'),
            dataIndex: 'latitude',
            key: 'latitude'
        },
        {
            title: t('LONGITUDE_COLUMN'),
            dataIndex: 'longitude',
            key: 'longitude'
        },
    ]

    if (showStatusColumn) {
        columns.push({
            title: t('STATUS_COLUMN'),
            key: 'status',
            render: (text, record) => {
                if (localStations.length === 0) {
                    return <QuestionCircleOutlined style={{ color: 'orange' }} />;
                }

                const existsInLocal = localStations.some(localStation => localStation.id === record.id);
                if (existsInLocal) {
                    return <CheckCircleOutlined style={{ color: 'green' }} />;
                } else {
                    return <CloseCircleOutlined style={{ color: 'red' }} />;
                }
            }
        });
    }

    return(
        <>
            <Table
                columns={columns}
                dataSource={stations}
                rowKey="id"
                pagination={{
                    defaultPageSize: 5,
                    showSizeChanger: true,
                    pageSizeOptions: ['5', '10', '15'],
                    current: pagination.current,
                    pageSize: pagination.pageSize
                }}
                onChange={handleTableChange}
                rowSelection={rowSelection}
            />
        </>
    )
}
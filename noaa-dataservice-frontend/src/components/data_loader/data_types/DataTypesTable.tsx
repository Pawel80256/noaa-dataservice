import {useTranslation} from "react-i18next";
import {useState} from "react";
import {Table, TableProps} from "antd";
import {NOAADataset} from "../../../models/NOAADataset";
import {NOAADataType} from "../../../models/NOAADataType";
import {CheckCircleOutlined, CloseCircleOutlined, QuestionCircleOutlined} from "@ant-design/icons";

export interface DataTypesTableProps {
    dataTypes: NOAADataType[],
    updateSelectedDataTypes: (keys: React.Key[]) => void,
    localDataTypes: NOAADataType[],
    showStatusColumn?: boolean
}

export const DataTypesTable = (
    {dataTypes, updateSelectedDataTypes, localDataTypes, showStatusColumn} : DataTypesTableProps
) => {
    const {t} = useTranslation();
    const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]);
    const [pagination, setPagination] = useState<{current:number,pageSize:number}>({ current: 1, pageSize: 5 });

    const handleTableChange = (pagination:any) => {
        setPagination({
            current: pagination.current,
            pageSize: pagination.pageSize
        });
    };

    const onSelectChange = (newSelectedRowKeys: React.Key[]) => {
        // console.log('selectedRowKeys changed: ', newSelectedRowKeys);
        setSelectedRowKeys(newSelectedRowKeys);
        updateSelectedDataTypes(newSelectedRowKeys)
    };

    const rowSelection = {
        selectedRowKeys,
        onChange: onSelectChange,
    };

    const columns: TableProps<NOAADataType>['columns'] = [
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
        }
    ]

    if (showStatusColumn) {
        columns.push({
            title: t('STATUS_COLUMN'),
            key: 'status',
            render: (text, record) => {
                if (localDataTypes.length === 0) {
                    return <QuestionCircleOutlined style={{ color: 'orange' }} />;
                }

                const existsInLocal = localDataTypes.some(localDataType => localDataType.id === record.id);
                if (existsInLocal) {
                    return <CheckCircleOutlined style={{ color: 'green' }} />;
                } else {
                    return <CloseCircleOutlined style={{ color: 'red' }} />;
                }
            }
        });
    }

    return (
        <>
            <Table
                columns={columns}
                dataSource={dataTypes}
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
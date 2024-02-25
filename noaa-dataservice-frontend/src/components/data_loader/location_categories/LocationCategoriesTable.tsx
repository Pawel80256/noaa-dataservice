import {NOAALocationCategory} from "../../../models/NOAALocationCategory";
import {useTranslation} from "react-i18next";
import {useEffect, useState} from "react";
import {Table, TableProps} from "antd";
import {CheckCircleOutlined, CloseCircleOutlined, QuestionCircleOutlined} from "@ant-design/icons";

export interface LocationCategoriesTableProps {
    locationCategories: NOAALocationCategory[],
    updateSelectedLocationCategories: (keys: React.Key[]) => void,
    localLocationCategories: NOAALocationCategory[],
    selectedLocationCategories:React.Key[]
    showStatusColumn?: boolean
}

export const LocationCategoriesTable = (
    {locationCategories,updateSelectedLocationCategories,localLocationCategories,selectedLocationCategories,showStatusColumn}:LocationCategoriesTableProps
) => {
    const {t} = useTranslation();
    const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]);
    const [pagination, setPagination] = useState<{current:number,pageSize:number}>({ current: 1, pageSize: 5 });

    useEffect(()=>{
        setSelectedRowKeys(selectedLocationCategories);
    },[selectedLocationCategories])

    const handleTableChange = (pagination:any) => {
        setPagination({
            current: pagination.current,
            pageSize: pagination.pageSize
        });
    };

    const onSelectChange = (newSelectedRowKeys: React.Key[]) => {
        setSelectedRowKeys(newSelectedRowKeys);
        updateSelectedLocationCategories(newSelectedRowKeys)
    };

    const rowSelection = {
        selectedRowKeys,
        onChange: onSelectChange,
    };

    const columns: TableProps<NOAALocationCategory>['columns'] = [
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
        }
    ]

    if (showStatusColumn) {
        columns.push({
            title: t('STATUS_COLUMN'),
            key: 'status',
            render: (text, record) => {
                if (localLocationCategories.length === 0) {
                    return <QuestionCircleOutlined style={{ color: 'orange' }} />;
                }

                const existsInLocal = localLocationCategories.some(localLocationCategory => localLocationCategory.id === record.id);
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
                dataSource={locationCategories}
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
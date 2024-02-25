import {Table, TableProps} from "antd";
import {useEffect, useState} from "react";

export interface DataTableProps {
    columns: TableProps<any>['columns'],
    data: any[],
    selectedData: React.Key[],
    updateSelectedData: (keys: React.Key[]) => void,
    showStatusColumn?: boolean,
    pagination: { current: number, pageSize: number },
    setPagination: (pagination: { current: number, pageSize: number }) => void
}

export const DataTable = ({columns,data,selectedData,updateSelectedData,showStatusColumn} : DataTableProps) => {
    const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]);
    const [pagination, setPagination] = useState<{current:number,pageSize:number}>({ current: 1, pageSize: 5 });

    useEffect(()=>{
        setSelectedRowKeys(selectedData)
    }, [selectedData])

    const handleTableChange = (newPagination:any) => {
        setPagination({
            current: newPagination.current,
            pageSize: newPagination.pageSize
        });
    };

    const onSelectChange = (newSelectedRowKeys: React.Key[]) => {
        setSelectedRowKeys(newSelectedRowKeys);
        updateSelectedData(newSelectedRowKeys)
    };

    const rowSelection = {
        selectedRowKeys,
        onChange: onSelectChange,
    };

    const paginationConfig = {
        defaultPageSize: 5,
        showSizeChanger: true,
        pageSizeOptions: ['5', '10', '15'],
        current: pagination.current,
        pageSize: pagination.pageSize
    }

    return(
        <>
            <Table
                columns={columns}
                dataSource={data}
                rowKey = "id"
                pagination={paginationConfig}
                onChange={handleTableChange}
                rowSelection={rowSelection}
            />
        </>
    )
}
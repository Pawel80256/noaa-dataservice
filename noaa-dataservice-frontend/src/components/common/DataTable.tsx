import {Input, Table, TableProps} from "antd";
import {useEffect, useState} from "react";

export interface DataTableProps {
    columns: TableProps<any>['columns'],
    data: any[],
    selectedData: React.Key[],
    updateSelectedData: (keys: React.Key[]) => void,
    pagination: { current: number, pageSize: number },
    setPagination: (pagination: { current: number, pageSize: number }) => void,
    searchableColumns?: string[]
}

export const DataTable = ({columns,data,selectedData,updateSelectedData, searchableColumns = []} : DataTableProps) => {
    const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]);
    const [pagination, setPagination] = useState<{current:number,pageSize:number}>({ current: 1, pageSize: 5 });

    const [filters, setFilters] = useState<{ [key: string]: string }>({});
    const handleSearch = (selectedColumn: string, value: string) => {
        setFilters({ ...filters, [selectedColumn]: value });
    };

    const filteredData = data.filter(row =>
        Object.entries(filters).every(([key, value]) =>
            row[key] ? row[key].toString().toLowerCase().includes(value.toLowerCase()) : false
        )
    );

    type FilterDropdownProps = {
        setSelectedKeys: (keys: string[]) => void;
        selectedKeys: string[];
        confirm: () => void;
    };

    const enhancedColumns = columns!.map(col => {
        if (searchableColumns.includes(col.key as string)) {
            return {
                ...col,
                filterDropdown: ({ setSelectedKeys, selectedKeys, confirm }: FilterDropdownProps) => (
                    <div style={{ padding: 8 }}>
                        <Input
                            placeholder={`Search ${col.title}`}
                            value={selectedKeys[0]}
                            onChange={e => handleSearch(col.key as string, e.target.value)}
                            style={{ marginBottom: 8, display: 'block' }}
                        />
                    </div>
                )
            };
        }
        return col;
    });

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
                // @ts-ignore
                columns={enhancedColumns}
                dataSource={filteredData}
                rowKey = "id"
                pagination={paginationConfig}
                onChange={handleTableChange}
                rowSelection={rowSelection}
            />
        </>
    )
}
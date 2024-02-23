import {NOAADataType} from "../../../models/NOAADataType";
import {NOAALocation} from "../../../models/NOAALocation";
import {useTranslation} from "react-i18next";
import {useEffect, useState} from "react";
import {Input, Space, Table, TableProps} from "antd";
import {CheckCircleOutlined, CloseCircleOutlined, QuestionCircleOutlined} from "@ant-design/icons";

export interface LocationsTableProps {
    locations: NOAALocation[],
    updateSelectedLocations?: (keys: React.Key[]) => void,
    localLocations?: NOAALocation[],
    selectedLocations?:React.Key[],
    showStatusColumn?: boolean,
    showParentColumn?:boolean,
    multiSelect?:boolean
    // locationCategory:string
}
export const LocationsTable = ({locations,updateSelectedLocations,localLocations,selectedLocations,showStatusColumn,showParentColumn,multiSelect=true}:LocationsTableProps) => {
    const {t} = useTranslation();
    const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]);
    const [pagination, setPagination] = useState<{current:number,pageSize:number}>({ current: 1, pageSize: 5 });
    const [filteredData, setFilteredData] = useState<NOAALocation[]>(locations);
    const [searchText, setSearchText] = useState<string>('');
    useEffect(()=>{
        if(selectedLocations){
            setSelectedRowKeys(selectedLocations)
        }
    }, [selectedLocations])

    useEffect(() => {
        const filtered = locations.filter(location =>
            location.id.toLowerCase().includes(searchText.toLowerCase()) ||
            location.name.toLowerCase().includes(searchText.toLowerCase()) ||
            location.datacoverage.toString().toLowerCase().includes(searchText.toLowerCase()) ||
            new Date(location.mindate).toISOString().toLowerCase().includes(searchText.toLowerCase()) ||
            new Date(location.maxdate).toISOString().toLowerCase().includes(searchText.toLowerCase())
        );
        setFilteredData(filtered);
    }, [searchText, locations]);


    const handleTableChange = (pagination: any, filters: any, sorter: any) => {
        setPagination({
            current: pagination.current,
            pageSize: pagination.pageSize
        });
    };

    const onSelectChange = (newSelectedRowKeys: React.Key[]) => {
        // console.log('selectedRowKeys changed: ', newSelectedRowKeys);
        setSelectedRowKeys(newSelectedRowKeys);
        if (updateSelectedLocations) {
            updateSelectedLocations(newSelectedRowKeys)
        }
    };

    const rowSelection = {
        selectedRowKeys,
        onChange: onSelectChange,
        type: multiSelect ? 'checkbox' as const : 'radio' as const,
    };

    const columns: TableProps<NOAADataType>['columns'] = [
        {
            title: t('INDEX_COLUMN'),
            key: 'index',
            render: (value, item, index) => (pagination.current - 1) * pagination.pageSize + index + 1
            // Sortowanie dla indeksu nie jest potrzebne
        },
        {
            title: t('IDENTIFIER_COLUMN'),
            dataIndex: 'id',
            key: 'id',
            sorter: (a, b) => a.id.localeCompare(b.id)
        },
        {
            title: t('NAME_COLUMN'),
            dataIndex: 'name',
            key: 'name',
            sorter: (a, b) => a.name.localeCompare(b.name)
        },
        {
            title: t('DATA_COVERAGE_COLUMN'),
            dataIndex: 'datacoverage',
            key: 'datacoverage',
            sorter: (a, b) => a.datacoverage - b.datacoverage
        },
        {
            title: t('MIN_DATE_COLUMN'),
            dataIndex: 'mindate',
            key: 'mindate',
            render: (text, record) => record && record.mindate ? new Date(record.mindate).toISOString().split('T')[0] : '',
            sorter: (a, b) => new Date(a.mindate).getTime() - new Date(b.mindate).getTime()
        },
        {
            title: t('MAX_DATE_COLUMN'),
            dataIndex: 'maxdate',
            key: 'maxdate',
            render: (text, record) => record && record.maxdate ? new Date(record.maxdate).toISOString().split('T')[0] : '',
            sorter: (a, b) => new Date(a.maxdate).getTime() - new Date(b.maxdate).getTime()
        }
        // Pozostałe definicje kolumn
    ];


    //todo: same for parent column
    if (showStatusColumn && localLocations) {
        columns.push({
            title: t('STATUS_COLUMN'),
            key: 'status',
            render: (text, record) => {
                if (localLocations.length === 0) {
                    return <QuestionCircleOutlined style={{ color: 'orange' }} />;
                }

                const existsInLocal = localLocations.some(localLocation => localLocation.id === record.id);
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
            <Space style={{ marginBottom: 16 }}>
                <Input placeholder="Search" onChange={e => setSearchText(e.target.value)} />
            </Space>
            <Table
                columns={columns}
                dataSource={filteredData}
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
    );
}
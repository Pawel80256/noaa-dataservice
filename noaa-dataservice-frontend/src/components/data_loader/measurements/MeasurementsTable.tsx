import {NOAAData} from "../../../models/NOAADataDto";
import {useTranslation} from "react-i18next";
import {useEffect, useState} from "react";
import {Table, TableProps} from "antd";
import {CheckCircleOutlined, CloseCircleOutlined} from "@ant-design/icons";

export interface MeasurementsTableProps {
    measurements: NOAAData[],
    updateSelectedMeasurements?: (keys: React.Key[]) => void,
    localMeasurements?: NOAAData[],
    selectedMeasurements?: React.Key[],
    showStatusColumn?: boolean,
}

export const MeasurementsTable = ({
                                      measurements,
                                      updateSelectedMeasurements,
                                      localMeasurements,
                                      selectedMeasurements,
                                      showStatusColumn
                                  }: MeasurementsTableProps) => {
    const {t} = useTranslation();
    const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]);
    const [pagination, setPagination] = useState<{ current: number, pageSize: number }>({current: 1, pageSize: 5});

    useEffect(() => {
        if (selectedMeasurements) {
            setSelectedRowKeys(selectedMeasurements)
        }
    }, [selectedMeasurements])

    const handleTableChange = (pagination: any) => {
        setPagination({
            current: pagination.current,
            pageSize: pagination.pageSize
        });
    };

    const onSelectChange = (newSelectedRowKeys: React.Key[]) => {
        // console.log('selectedRowKeys changed: ', newSelectedRowKeys);
        setSelectedRowKeys(newSelectedRowKeys);
        if (updateSelectedMeasurements) {
            updateSelectedMeasurements(newSelectedRowKeys)
        }
    };

    const rowSelection = {
        selectedRowKeys,
        onChange: onSelectChange,
    };

    const columns: TableProps<NOAAData>['columns'] = [
        {
            title: t('INDEX_COLUMN'),
            key: 'index',
            render: (value, item, index) => (pagination.current - 1) * pagination.pageSize + index + 1
        },
        // {
        //     title: t('IDENTIFIER_COLUMN'),
        //     dataIndex:'id',
        //     key:'id'
        // },
        {
            title: t('DATA_TYPE_COLUMN'),
            dataIndex: 'dataTypeId',
            key: 'dataTypeId'
        },
        {
            title: t('DATE_COLUMN'),
            dataIndex: 'date',
            key: 'date',
        },
        {
            title: t('VALUE_COLUMN'),
            dataIndex: 'value',
            key: 'value',
        },
        {
            title: t('ATTRIBUTES_COLUMN'),
            dataIndex: 'attributes',
            key: 'attributes',
        },
        {
            title: t('STATION_COLUMN'),
            dataIndex: 'stationId',
            key: 'stationId',
        },
        {
            title: t('STATUS_COLUMN'),
            dataIndex: 'loaded',
            key: 'loaded',
            render: (value, item, index) => {
                if (value) {
                    return <CheckCircleOutlined style={{color: 'green'}}/>;
                } else {
                    return <CloseCircleOutlined style={{color: 'red'}}/>;
                }
            }
        },

    ]

    // if (showStatusColumn) {
    //     columns.push({
    //         title: t('STATUS_COLUMN'),
    //         key: 'status',
    //         render: (text, record) => {
    //             if (localMeasurements?.length === 0) {
    //                 return <QuestionCircleOutlined style={{ color: 'orange' }} />;
    //             }
    //
    //             const existsInLocal = localMeasurements?.some(localMeasurement => localMeasurement.id === record.id);
    //             if (existsInLocal) {
    //                 return <CheckCircleOutlined style={{ color: 'green' }} />;
    //             } else {
    //                 return <CloseCircleOutlined style={{ color: 'red' }} />;
    //             }
    //         }
    //     });
    // }

    return (
        <>
            <Table
                columns={columns}
                dataSource={measurements}
                rowKey="id"
                pagination={{
                    defaultPageSize: 5,
                    showSizeChanger: true,
                    pageSizeOptions: ['5', '10', '15'],
                    current: pagination.current,
                    pageSize: pagination.pageSize
                }}
                onChange={handleTableChange}
                // rowSelection={rowSelection}
            />
        </>
    )
}
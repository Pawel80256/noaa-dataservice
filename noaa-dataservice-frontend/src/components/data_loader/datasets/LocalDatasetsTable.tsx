import {Table, TableProps} from "antd";
import {NOAADataset} from "../../../models/NOAADataset";
import {useTranslation} from "react-i18next";
import {PaginationWrapper} from "../../../models/PaginationWrapper";
import {useState} from "react";
import {RemoteDatasetsTableProps} from "./RemoteDatasetsTable";

export interface LocalDatasetsTableProps {
    datasets: PaginationWrapper<NOAADataset>;
}
export const LocalDatasetsTable = ({ datasets }: LocalDatasetsTableProps) => {
    const {t} = useTranslation();

    const columns: TableProps<NOAADataset>['columns'] = [
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

    return (
        <>
            <Table
                columns={columns}
                dataSource={datasets.data}
                pagination={{
                    defaultPageSize:5,
                    showSizeChanger:true,
                    pageSizeOptions:['5','10','15']
                }}/>
        </>
    )
}
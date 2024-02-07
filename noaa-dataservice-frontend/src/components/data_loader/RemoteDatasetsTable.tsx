import {Table, TableProps} from "antd";
import {NOAADataset} from "../../models/NOAADataset";
import {useTranslation} from "react-i18next";

export const RemoteDatasetsTable = () => {
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
            render: (text, record) => record.mindate.toISOString().split('T')[0]
        },
        {
            title: t('MAX_DATE_COLUMN'),
            dataIndex:'maxdate',
            key:'maxdate',
            render: (text, record) => record.maxdate.toISOString().split('T')[0]
        }
    ]

    const data: NOAADataset[] = [
        {uid:'elo',mindate: new Date('2021-01-15'), maxdate: new Date('2024-01-25'), name:'nazwa', datacoverage:0.78,id:'idd'},
        {uid:'el2o',mindate: new Date('2022-03-15'), maxdate: new Date('2023-07-26'), name:'nazwa2', datacoverage:0.78,id:'idd2'},
    ]

    return (
        <>
            <Table columns={columns} dataSource={data}/>
        </>
    )
}
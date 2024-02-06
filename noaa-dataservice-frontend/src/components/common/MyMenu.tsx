import {Dropdown, Menu, MenuProps} from "antd";
import {AppstoreOutlined, MailOutlined, SettingOutlined, GlobalOutlined } from '@ant-design/icons';
import {useTranslation} from "react-i18next";
import {useState} from "react";

export const MyMenu = () => {

    const{t, i18n} = useTranslation();
    const [language, setLanguage] = useState("en");


    type MenuItem = Required<MenuProps>['items'][number];

    const handleLanguageChange = (languageCode:string)  => {
        i18n.changeLanguage(languageCode);
    }
    const onClick: MenuProps['onClick'] = (e) => {
        if(e.key == 'en' || e.key == 'pl'){
            handleLanguageChange(e.key);
        }
        console.log('click ', e);
    };

    function getItem(
        label: React.ReactNode,
        key: React.Key,
        icon?: React.ReactNode,
        children?: MenuItem[],
        type?: 'group',
    ): MenuItem {
        return {
            key,
            icon,
            children,
            label,
            type,
        } as MenuItem;
    }

    const languages: MenuProps['items'] = [
        getItem('English', 'en'),
        getItem('Polish', 'pl')
    ];

    const languages2: MenuProps['items'] = [
        getItem("English","en"),
        getItem("Polish","pl")
    ];

    const items: MenuProps['items'] = [
        getItem(t('DATA_LOADER'), 'dataLoader', <MailOutlined />, [
            getItem(t('DATA_SETS'), 'datasets', null),
            getItem(t('DATA_TYPES'), 'dataTypes', null),
            getItem(t('LOCATION_CATEGORIES'), 'locationCategories', null),
            getItem(t('LOCATIONS'), 'locations', null),
            getItem(t('STATIONS'), 'stations', null),
            getItem(t('MEASUREMENTS'), 'measurements', null),
        ]),

        getItem(t('DATA_VIEWER'), 'dataViewer', <AppstoreOutlined />, [
            getItem('<in develompent>', 'inDevelopment1'),
        ]),


        getItem(t('DATA_MANAGER'), 'dataManager', <SettingOutlined />, [
            getItem('<in develompent>', 'inDevelopment2')
        ]),

        getItem(t('LANGUAGE'), 'lang', <SettingOutlined />, [
            getItem('English', 'en'),
            getItem('Polski', 'pl')
        ]),

    ];


    return (
        <>
            <Menu
                onClick={onClick}
                style={{ width: 256 }}
                defaultSelectedKeys={['1']}
                defaultOpenKeys={['sub1']}
                mode="inline"
                items={items}
            />
        </>
    )
}
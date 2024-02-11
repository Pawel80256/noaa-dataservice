import i18n from 'i18next';
import { notification } from 'antd';

export const showSuccessNotification = (description: string) => {
    notification.success({
        message: i18n.t('SUCCESS_LABEL'),
        description: description,
        placement: 'bottomRight'
    });
};

export const showErrorNotification = (description: string) => {
    notification.error({
        message: i18n.t('ERROR_LABEL'),
        description: description,
        placement: 'bottomRight'
    });
};
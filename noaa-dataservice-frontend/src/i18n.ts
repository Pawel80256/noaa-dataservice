import i18n from 'i18next';
import {initReactI18next} from 'react-i18next';
import plLanguage from './i18n_languages/pl.json'
import enLanguage from './i18n_languages/en.json'

// const loadLocaleData = (locale:any) => {
//     switch (locale) {
//         case 'en':
//             return import('./i18n_languages/en.json');
//         case 'pl':
//             return import('./i18n_languages/pl.json');
//         default:
//             return import('./i18n_languages/en.json');
//     }
// };

const resources = {
    en: {
        translation: enLanguage
    },
    pl: {
        translation: plLanguage
    }
};

i18n
    .use(initReactI18next)
    .init({
        resources
        //     : {
        //     en: {
        //         translation: loadLocaleData('en'),
        //     },
        //     pl: {
        //         translation: loadLocaleData('pl'),
        //     },
        // }
        ,
        lng: 'en',
        // fallbackLng: 'en',
        interpolation: {
            escapeValue: false,
        },
    });

export default i18n;

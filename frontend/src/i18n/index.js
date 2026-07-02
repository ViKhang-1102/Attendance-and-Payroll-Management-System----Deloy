
import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import vi from './vi';
import en from './en';

const resources = {
  vi: { translation: vi },
  en: { translation: en }
};

i18n
  .use(initReactI18next)
  .init({
    resources,
    lng: 'vi', // Default language
    fallbackLng: 'vi',
    interpolation: {
      escapeValue: false
    }
  });

export default i18n;

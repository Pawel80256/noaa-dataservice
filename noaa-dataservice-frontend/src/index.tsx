import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import reportWebVitals from './reportWebVitals';
import './i18n';
import {createBrowserRouter, Outlet, RouterProvider,} from "react-router-dom";
import {DatasetLoaderView} from "./views/DatasetLoaderView";
import {MyMenu} from "./components/common/MyMenu";
import {Col, Row} from "antd";
import {DataTypesLoaderView} from "./views/DataTypesLoaderView";
import {LocationCategoriesLoaderView} from "./views/LocationCategoriesLoaderView";
import {CountriesLoaderView} from "./views/CountriesLoaderView";
import {CitiesLoaderView} from "./views/CitiesLoaderView";
import {StatesLoaderView} from "./views/StatesLoaderView";
import {StationsLoaderView} from "./views/StationsLoaderView";
import {MeasurementsLoaderView} from "./views/MeasurementsLoaderView";

const Content = () => (
    <Row style={{ width: '100%', height: '100vh' }}>
        <Col style={{ width: '250px' }}>
            <MyMenu />
        </Col>
        <Col style={{ flex: 1 }}>
            <Outlet />
        </Col>
    </Row>
);

const router = createBrowserRouter([
    {
        path: "/",
        element: <Content />,
        children: [
            { path: "test", element: <div>Hello world!</div> },
            { path: "dataloader/datasets", element: <DatasetLoaderView /> },
            { path: "dataloader/datatypes", element: <DataTypesLoaderView /> },
            { path: "dataloader/locationcategories", element: <LocationCategoriesLoaderView /> },
            { path: "dataloader/locations/countries", element: <CountriesLoaderView /> },
            { path: "dataloader/locations/cities", element: <CitiesLoaderView /> },
            { path: "dataloader/locations/states", element: <StatesLoaderView /> },
            { path: "dataloader/stations", element: <StationsLoaderView /> },
            { path: "dataloader/measurements", element: <MeasurementsLoaderView /> },
        ],
    },
]);

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);
root.render(
  <React.StrictMode>
      <RouterProvider router={router} />
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();

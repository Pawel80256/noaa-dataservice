import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import './i18n';
import {
    createBrowserRouter,
    RouterProvider,
} from "react-router-dom";
import {DatasetLoaderView} from "./views/DatasetLoaderView";
import {MyMenu} from "./components/common/MyMenu";
import {Row} from "antd";

const router = createBrowserRouter([
    {
        path: "/test",
        element: <div>Hello world!</div>,
    },
    {
        path:"/dataloader/datasets",
        element:<DatasetLoaderView/>
    }
]);

const Content = () => (
    <Row>
        <MyMenu />
        <RouterProvider router={router} />
    </Row>
);

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);
root.render(
  <React.StrictMode>
      <Content />
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();

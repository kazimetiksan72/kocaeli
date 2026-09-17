import React from 'react';import ReactDOM from 'react-dom/client';import {QueryClient,QueryClientProvider}from'@tanstack/react-query';import {CssBaseline}from'@mui/material';import {BrowserRouter}from'react-router-dom';import App from'./App';import'maplibre-gl/dist/maplibre-gl.css';import'./styles.css';
ReactDOM.createRoot(document.getElementById('root')!).render(<React.StrictMode><QueryClientProvider client={new QueryClient({defaultOptions:{queries:{staleTime:15_000,retry:1}}})}><BrowserRouter><CssBaseline/><App/></BrowserRouter></QueryClientProvider></React.StrictMode>);


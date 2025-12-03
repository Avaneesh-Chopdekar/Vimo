import React from "react";
import { createRoot } from "react-dom/client";
import {
  BrowserRouter,
  Route,
  Routes,
  useHref,
  useNavigate,
  type NavigateOptions,
} from "react-router";
import { defaultTheme, Provider } from "@adobe/react-spectrum";

import "./style.css";
import HomePage from "./pages/Home";
import UploadPage from "./pages/Upload";
import StreamPage from "./pages/Stream";

declare module "@adobe/react-spectrum" {
  interface RouterConfig {
    routerOptions: NavigateOptions;
  }
}

const App = () => {
  const navigate = useNavigate();
  return (
    <Provider
      theme={defaultTheme}
      colorScheme="light"
      router={{ navigate, useHref }}
    >
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/upload" element={<UploadPage />} />
        <Route path="/stream/:id" element={<StreamPage />} />
      </Routes>
    </Provider>
  );
};

createRoot(document.getElementById("app")!).render(
  <BrowserRouter>
    <App />
  </BrowserRouter>,
);

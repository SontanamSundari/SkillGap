import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";

import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import MySkills from "./pages/MySkills";
import JobDetails from "./pages/JobDetails";
import Learning from "./pages/Learning";
import Assessment from "./pages/Assessment";
import ProtectedRoute from "./routes/ProtectedRoute";
import "./App.css";
import Register from "./pages/Register";
import AddJob from "./pages/AddJob";


function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        <Route
          path="/dashboard"
          element={
            <ProtectedRoute>
              <Dashboard />
            </ProtectedRoute>
          }
        />

        <Route
          path="/skills"
          element={
            <ProtectedRoute>
              <MySkills />
            </ProtectedRoute>
          }
        />

        <Route
          path="/jobs/:jobId"
          element={
            <ProtectedRoute>
              <JobDetails />
            </ProtectedRoute>
          }
        />

        <Route
          path="/learning"
          element={
            <ProtectedRoute>
              <Learning />
            </ProtectedRoute>
          }
        />


<Route
  path="/jobs/:jobId/assessment"
  element={
    <ProtectedRoute>
      <Assessment />
    </ProtectedRoute>
  }
/>

<Route
  path="/jobs/:jobId/learning"
  element={
    <ProtectedRoute>
      <Learning />
    </ProtectedRoute>
  }
/>

<Route
  path="/jobs/:jobId/skills/:skillId/assessment"
  element={
    <ProtectedRoute>
      <Assessment />
    </ProtectedRoute>
  }
/>


        <Route path="/" element={<Navigate to="/login" replace />} />
        <Route
          path="/jobs/new"
          element={
            <ProtectedRoute>
              <AddJob />
            </ProtectedRoute>
          }
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;

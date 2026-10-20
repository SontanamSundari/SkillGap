import { createContext, useContext, useState } from "react";

const AuthContext = createContext(null);

export function AuthProvider({children}){
    const[token,setToken] = useState(()=> localStorage.getItem("skillgap_token"));
    const login = (newToken) => {
        localStorage.setItem("skillgap_token", newToken);
        setToken(newToken);
    };
    const logout = () => {
        localStorage.removeItem("skillgap_token");
        setToken(null);
    };
    const isAuthenticated=!!token;
    return(
        <AuthContext.Provider
        value={{
            token,
            isAuthenticated,
            login,
            logout,
        }}
        >
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth(){
    return useContext(AuthContext);
}
// src/App.tsx
import React from 'react';
import { Routes, Route } from 'react-router-dom';
import Conversations from './components/Conversations';
import Chat from './components/Chat';

const App: React.FC = () => {
    return (
        <Routes>
            <Route path="/" element={<Conversations />} />
            <Route path="/chat/:conversationId" element={<Chat />} />
        </Routes>
    );
};

export default App;

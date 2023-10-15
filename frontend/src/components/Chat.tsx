// src/components/Chat.tsx
import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { connect, disconnect, sendMessage } from '../websocket';

const Chat: React.FC = () => {
    const { conversationId } = useParams<{ conversationId: string }>();
    const safeConversationId = conversationId || 'default-conversation-id';
    const [messages, setMessages] = useState<Array<{ content: string }>>([]);
    const [newMessage, setNewMessage] = useState<string>('');

    const fetchMessages = async () => {
        try {
            const response = await fetch(`http://localhost:8080/chat/${conversationId}/messages`);
            if (!response.ok) throw new Error('Network response was not ok ' + response.statusText);
            const data = await response.json();
            setMessages(data);
        } catch (error) {
            console.error('There has been a problem with your fetch operation:', error);
        }
    };

    useEffect(() => {
        fetchMessages();
    }, [conversationId]);

    useEffect(() => {
        function handleNewMessage(message: { content: string }) {
            setMessages(prevMessages => [...prevMessages, message]);
        }

        connect(safeConversationId, handleNewMessage);  // Connect to WebSocket and setup message handling

        return () => {
            disconnect();  // Disconnect the WebSocket when the component is unmounted
        };
    }, [safeConversationId]);

    const handleSendMessage = (): void => {
        if (newMessage.trim()) {  // Prevent sending empty messages
            sendMessage(`/chat/${safeConversationId}/sendMessage`, { content: newMessage });
            setNewMessage('');
        }
    };

    return (
        <div className="p-6 max-w-md mx-auto bg-white rounded-xl shadow-md flex flex-col space-y-4">
            <div className="flex-1 overflow-auto">
                {messages.map((message, index) => (
                    <div key={index} className="p-2 bg-gray-100 rounded mb-2">{message.content}</div>
                ))}
            </div>
            <div className="flex space-x-2">
                <input
                    value={newMessage}
                    onChange={(e) => setNewMessage(e.target.value)}
                    placeholder="Type a message..."
                    className="w-full p-2 border rounded"
                    onKeyPress={(e) => e.key === 'Enter' && handleSendMessage()}  // Send message on Enter key press
                />
                <button onClick={handleSendMessage} className="bg-blue-500 text-white p-2 rounded">Send</button>
            </div>
        </div>
    );
};

export default Chat;

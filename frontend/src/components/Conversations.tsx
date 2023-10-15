// src/components/Conversations.tsx
import React, {useEffect, useState} from 'react';
import {Link} from 'react-router-dom';

const Conversations: React.FC = () => {
    const [conversations, setConversations] = useState<Array<{ id: string }>>([]);

    const fetchConversations = async () => {
        try {
            const response = await fetch('http://localhost:8080/chat');
            if (!response.ok) throw new Error('Network response was not ok ' + response.statusText);
            const data = await response.json();
            setConversations(data);
        } catch (error) {
            console.error('There has been a problem with your fetch operation:', error);
        }
    };

    useEffect(() => {
        fetchConversations();
    }, []);

    const createConversation = async () => {
        try {
            const response = await fetch('http://localhost:8080/chat', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
            });
            if (!response.ok) throw new Error('Network response was not ok ' + response.statusText);
            fetchConversations();  // Fetch conversations again to update the view
        } catch (error) {
            console.error('There has been a problem with your fetch operation:', error);
        }
    };

    return (
        <div className='mt-5'>
            <div className='flex justify-center'>
                <button className='bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded'
                        onClick={createConversation}>Create New Conversation
                </button>
            </div>
            <div className='flex justify-center mt-5'>
                <ul className='block'>
                    {conversations.map(conversation => (
                        <li key={conversation.id} className='p-2 border-2 border-blue-500 rounded'>
                            <Link to={`/chat/${conversation.id}`}>Conversation {conversation.id}</Link>
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );
};

export default Conversations;

import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

let stompClient: Client | null = null;

export function connect(conversationId: string, onMessageReceived: (message: { content: string }) => void): void {
    const socket = new SockJS('http://localhost:8080/chat');
    stompClient = new Client({
        webSocketFactory: () => socket,
        onConnect: () => {
            console.log('Connected');
            stompClient?.subscribe(`/topic/chat/${conversationId}`, (message) => {
                onMessageReceived(JSON.parse(message.body));
            });
        },
        onStompError: (error) => {
            console.error('STOMP error:', error);
        },
        onWebSocketClose: () => {
            console.log('Disconnected');
        }
    });
    stompClient.activate();
}

export function disconnect(): void {
    if (stompClient !== null) {
        stompClient.deactivate();
    }
    console.log('Disconnected');
}

export function sendMessage(destination: string, message: { content: string }): void {
    if (stompClient !== null && stompClient.connected) {
        stompClient.publish({ destination: `/app${destination}`, body: JSON.stringify(message) });
    } else {
        console.error('STOMP client is not connected');
    }
}

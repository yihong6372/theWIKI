declare module 'event-source-polyfill' {
    export class EventSourcePolyfill {
        constructor(url: string, config?: any);
        onmessage: (event: MessageEvent) => void;
        onerror: (event: Event) => void;
        close(): void;
    }
}
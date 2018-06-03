export interface Result {
    id?: number;
    timeStamp: string;
    elapsed : string;
	label : string;
	responseCode : string;
	responseMessage : string;
	threadName : string;
	dataType : string;
	success : string;
    failureMessage : string;
	bytes : string;
    sentBytes : string;
	grpThreads : string;
	allThreads : string;
	latency : string;
    idleTime : string;
    connect : string;
}

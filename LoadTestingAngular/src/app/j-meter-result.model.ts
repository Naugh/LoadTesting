import {Result} from "./result.model";

export interface JMeterResult {
    id? : number;
    results? : Array<Result>;
    systemDate? : String;
    name : String;
}

import {Transaction} from "./transaction";

export class DuplicateTransactions {
    origin: Transaction;
    duplicates: Transaction[];
}
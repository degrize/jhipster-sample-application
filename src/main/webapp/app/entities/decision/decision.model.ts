import { INouveau } from 'app/entities/nouveau/nouveau.model';
import { DecisionType } from 'app/entities/enumerations/decision-type.model';

export interface IDecision {
  id?: number;
  decision?: DecisionType;
  nouveaus?: INouveau[] | null;
}

export class Decision implements IDecision {
  constructor(public id?: number, public decision?: DecisionType, public nouveaus?: INouveau[] | null) {}
}

export function getDecisionIdentifier(decision: IDecision): number | undefined {
  return decision.id;
}

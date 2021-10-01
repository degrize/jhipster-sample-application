import { IGem } from 'app/entities/gem/gem.model';

export interface IGuard {
  id?: number;
  guard?: IGem | null;
  gems?: IGem[] | null;
}

export class Guard implements IGuard {
  constructor(public id?: number, public guard?: IGem | null, public gems?: IGem[] | null) {}
}

export function getGuardIdentifier(guard: IGuard): number | undefined {
  return guard.id;
}

import * as dayjs from 'dayjs';
import { IImageCulte } from 'app/entities/image-culte/image-culte.model';
import { INouveau } from 'app/entities/nouveau/nouveau.model';

export interface ICulte {
  id?: number;
  theme?: string;
  date?: dayjs.Dayjs;
  imageCultes?: IImageCulte[] | null;
  nouveaus?: INouveau[] | null;
}

export class Culte implements ICulte {
  constructor(
    public id?: number,
    public theme?: string,
    public date?: dayjs.Dayjs,
    public imageCultes?: IImageCulte[] | null,
    public nouveaus?: INouveau[] | null
  ) {}
}

export function getCulteIdentifier(culte: ICulte): number | undefined {
  return culte.id;
}

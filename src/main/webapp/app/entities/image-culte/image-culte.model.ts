import { ICulte } from 'app/entities/culte/culte.model';
import { IDepartement } from 'app/entities/departement/departement.model';

export interface IImageCulte {
  id?: number;
  titre?: string;
  imageContentType?: string;
  image?: string;
  cultes?: ICulte[] | null;
  departements?: IDepartement[] | null;
}

export class ImageCulte implements IImageCulte {
  constructor(
    public id?: number,
    public titre?: string,
    public imageContentType?: string,
    public image?: string,
    public cultes?: ICulte[] | null,
    public departements?: IDepartement[] | null
  ) {}
}

export function getImageCulteIdentifier(imageCulte: IImageCulte): number | undefined {
  return imageCulte.id;
}

import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'culte',
        data: { pageTitle: 'jhipsterSampleApplicationApp.culte.home.title' },
        loadChildren: () => import('./culte/culte.module').then(m => m.CulteModule),
      },
      {
        path: 'image-culte',
        data: { pageTitle: 'jhipsterSampleApplicationApp.imageCulte.home.title' },
        loadChildren: () => import('./image-culte/image-culte.module').then(m => m.ImageCulteModule),
      },
      {
        path: 'nouveau',
        data: { pageTitle: 'jhipsterSampleApplicationApp.nouveau.home.title' },
        loadChildren: () => import('./nouveau/nouveau.module').then(m => m.NouveauModule),
      },
      {
        path: 'decision',
        data: { pageTitle: 'jhipsterSampleApplicationApp.decision.home.title' },
        loadChildren: () => import('./decision/decision.module').then(m => m.DecisionModule),
      },
      {
        path: 'frere-qui-invite',
        data: { pageTitle: 'jhipsterSampleApplicationApp.frereQuiInvite.home.title' },
        loadChildren: () => import('./frere-qui-invite/frere-qui-invite.module').then(m => m.FrereQuiInviteModule),
      },
      {
        path: 'communaute',
        data: { pageTitle: 'jhipsterSampleApplicationApp.communaute.home.title' },
        loadChildren: () => import('./communaute/communaute.module').then(m => m.CommunauteModule),
      },
      {
        path: 'ville',
        data: { pageTitle: 'jhipsterSampleApplicationApp.ville.home.title' },
        loadChildren: () => import('./ville/ville.module').then(m => m.VilleModule),
      },
      {
        path: 'quartier',
        data: { pageTitle: 'jhipsterSampleApplicationApp.quartier.home.title' },
        loadChildren: () => import('./quartier/quartier.module').then(m => m.QuartierModule),
      },
      {
        path: 'departement',
        data: { pageTitle: 'jhipsterSampleApplicationApp.departement.home.title' },
        loadChildren: () => import('./departement/departement.module').then(m => m.DepartementModule),
      },
      {
        path: 'besoin',
        data: { pageTitle: 'jhipsterSampleApplicationApp.besoin.home.title' },
        loadChildren: () => import('./besoin/besoin.module').then(m => m.BesoinModule),
      },
      {
        path: 'gem',
        data: { pageTitle: 'jhipsterSampleApplicationApp.gem.home.title' },
        loadChildren: () => import('./gem/gem.module').then(m => m.GemModule),
      },
      {
        path: 'guard',
        data: { pageTitle: 'jhipsterSampleApplicationApp.guard.home.title' },
        loadChildren: () => import('./guard/guard.module').then(m => m.GuardModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}

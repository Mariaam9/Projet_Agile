/* Salleo : interface du modèle fourni, palette bleu/gris, branchée sur l'API Spring Boot.
   Sans backend HTTP, les opérations basculent explicitement en démonstration locale. */
'use strict';
const $ = (s, parent=document) => parent.querySelector(s);
const root = $('#root');
const PATHS = {
  calendar:'<rect x="3" y="5" width="18" height="16" rx="2"/><path d="M16 3v4M8 3v4M3 10h18"/>',
  check:'<path d="m5 12 4 4L19 6"/>', arrow:'<path d="M5 12h14m-6-6 6 6-6 6"/>',
  sparkles:'<path d="m12 3 1.5 5 5 1.5-5 1.5L12 16l-1.5-5L5 9.5 10.5 8 12 3ZM5 17l.7 2.3L8 20l-2.3.7L5 23l-.7-2.3L2 20l2.3-.7L5 17Z"/>',
  shield:'<path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10Z"/><path d="m9 12 2 2 4-4"/>',
  building:'<rect x="4" y="2" width="16" height="20" rx="2"/><path d="M9 22v-4h6v4M8 7h1M15 7h1M8 11h1M15 11h1"/>',
  door:'<rect x="3" y="3" width="18" height="18" rx="2"/><path d="M9 21V3M13 12h.01"/>',
  users:'<path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2m16 0v-2a4 4 0 0 0-3-3.87M9 11a4 4 0 1 0 0-8 4 4 0 0 0 0 8ZM16 3.13a4 4 0 0 1 0 7.75"/>',
  bar:'<path d="M3 3v18h18M7 16v-5M12 16V7M17 16V5"/>',
  clock:'<circle cx="12" cy="12" r="9"/><path d="M12 7v5l3 2"/>',
  menu:'<path d="M4 7h16M4 12h16M4 17h16"/>',
  logout:'<path d="M10 17l5-5-5-5M15 12H3M12 3h6a3 3 0 0 1 3 3v12a3 3 0 0 1-3 3h-6"/>',
  search:'<circle cx="11" cy="11" r="7"/><path d="m16 16 5 5"/>', bell:'<path d="M18 8a6 6 0 0 0-12 0c0 7-3 7-3 9h18c0-2-3-2-3-9M10 21h4"/>',
  plus:'<path d="M12 5v14M5 12h14"/>', x:'<path d="M5 5l14 14M19 5 5 19"/>',
  grid:'<rect x="3" y="3" width="7" height="7" rx="1"/><rect x="14" y="3" width="7" height="7" rx="1"/><rect x="3" y="14" width="7" height="7" rx="1"/><rect x="14" y="14" width="7" height="7" rx="1"/>',
  down:'<path d="m6 9 6 6 6-6"/>', edit:'<path d="M12 20h9M16.5 3.5a2.12 2.12 0 0 1 3 3L9 17l-4 1 1-4L16.5 3.5Z"/>',
  trash:'<path d="M3 6h18M8 6V4h8v2M6 6l1 15h10l1-15M10 10v7M14 10v7"/>',
  left:'<path d="m15 18-6-6 6-6"/>', right:'<path d="m9 18 6-6-6-6"/>', info:'<circle cx="12" cy="12" r="10"/><path d="M12 11v6M12 7h.01"/>'
};
function ico(name,size=19){return `<svg width="${size}" height="${size}" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">${PATHS[name]||PATHS.grid}</svg>`;}
function esc(str=''){return String(str??'').replace(/[&<>"']/g,s=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[s]));}
const dateNow=()=>new Date();
function isoLocal(date){let d=new Date(date.getTime()-date.getTimezoneOffset()*60000); return d.toISOString().slice(0,16);}
function firstLastName(str=''){return str.split(' ').filter(Boolean).map(s=>s[0]).slice(0,2).join('').toUpperCase();}
function fmtDate(v,opt={day:'2-digit',month:'short',year:'numeric'}){return new Intl.DateTimeFormat('fr-FR',opt).format(new Date(v));}
function fmtTime(v){return new Intl.DateTimeFormat('fr-FR',{hour:'2-digit',minute:'2-digit'}).format(new Date(v));}
function sameDay(v, day){const d=new Date(v);return d.toDateString()===day.toDateString();}
function eMsg(err){return String(err?.message||err||'Erreur inconnue');}
function demoRooms(){return [
 {id:1,nom:'Atlas',localisation:'Étage 2 · Aile Nord',capacite:12,statut:'DISPONIBLE'},
 {id:2,nom:'Horizon',localisation:'Étage 1 · Aile Est',capacite:8,statut:'DISPONIBLE'},
 {id:3,nom:'Nova',localisation:'Étage 3 · Aile Sud',capacite:18,statut:'DISPONIBLE'},
 {id:4,nom:'Studio',localisation:'Étage 2 · Aile Ouest',capacite:5,statut:'MAINTENANCE'} ];}
function demoBookings(){const d=new Date();d.setDate(d.getDate()+1); d.setHours(0,0,0,0);
 const at=(h,m=0)=>{const n=new Date(d);n.setHours(h,m);return isoLocal(n)};
 return [{id:101,employe:'Sami Trabelsi',salle:'Atlas',salleId:1,employeId:1,dateDebut:at(9,30),dateFin:at(10,30),motif:'Réunion projet Orion',participants:6,statut:'CONFIRMEE'},
 {id:102,employe:'Amira Ben Salah',salle:'Horizon',salleId:2,employeId:2,dateDebut:at(14,0),dateFin:at(15,0),motif:'Atelier produit',participants:5,statut:'CONFIRMEE'}];}
const users={ 'sami@example.com':{id:1,nom:'Sami Trabelsi',role:'Employé'},'amira@example.com':{id:2,nom:'Amira Ben Salah',role:'Employée'},'responsable@example.com':{id:4,nom:'Nadia Mansour',role:'Responsable'} };
const state={user:null,view:'dashboard',backend:false,rooms:[],bookings:[],modal:null,menu:false,toast:null,loginError:'',search:'',calMonth:new Date(),dateRange:null};
try{state.user=JSON.parse(sessionStorage.getItem('salleo.user')||'null')}catch{}
function isResponsible(){return state.user?.role==='Responsable'}
function currentBookings(){return state.bookings.filter(b=>b.statut!=='ANNULEE');}
function overlaps(a,b,c,d){return new Date(a)<new Date(d)&&new Date(b)>new Date(c)}
function occupied(room,from,to){if(room.statut!=='DISPONIBLE')return true;return currentBookings().some(b=>b.salle===room.nom && overlaps(b.dateDebut,b.dateFin,from,to));}
function availableNow(room){const start=new Date();const end=new Date(start.getTime()+30*60000);return !occupied(room,start,end)}
function availableCount(){return state.rooms.filter(availableNow).length}
function occupancy(){return state.rooms.length?Math.round((1-availableCount()/state.rooms.length)*100):0}
function showToast(t,err=false){state.toast={text:t,error:err};render();clearTimeout(showToast.timer);showToast.timer=setTimeout(()=>{state.toast=null;render()},4300);}
async function loadData(){
 try{
  if(location.protocol!=='http:'&&location.protocol!=='https:')throw Error('Aucun serveur disponible');
  const a=await fetch('/api/v1/salles',{headers:{Accept:'application/json'}});const b=await fetch('/api/v1/reservations',{headers:{Accept:'application/json'}});
  if(!a.ok||!b.ok)throw Error('API indisponible');
  const rooms=await a.json(),bookings=await b.json();
  if(!Array.isArray(rooms)||!Array.isArray(bookings))throw Error('API invalide');
  state.rooms=rooms;state.bookings=bookings.map(b=>({...b,participants:b.participants??1,salleId:b.salleId??rooms.find(r=>r.nom===b.salle)?.id,employeId:b.employeId??1}));state.backend=true;
 }catch{
  state.backend=false;state.rooms=demoRooms();let stored=null;
  try{stored=JSON.parse(localStorage.getItem('salleo.demo.bookings')||'null')}catch{}
  state.bookings=Array.isArray(stored)?stored:demoBookings();
 }
 render();
}
function persistDemo(){try{localStorage.setItem('salleo.demo.bookings',JSON.stringify(state.bookings))}catch{}}
async function saveBooking(form){
 const fd=new FormData(form);const salleId=Number(fd.get('salleId'));
 const room=state.rooms.find(r=>r.id===salleId);
 const d1=String(fd.get('dateDebut')),d2=String(fd.get('dateFin')),parts=Number(fd.get('nombreParticipants'));
 const motif=String(fd.get('motif')||'').trim();
 if(!room||!motif||!(new Date(d1)<new Date(d2)))throw Error('Vérifie la salle, les horaires et le motif.');
 if(room.statut!=='DISPONIBLE')throw Error('Salle indisponible ou en maintenance.');
 if(parts<1||parts>room.capacite)throw Error(`Nombre de participants invalide (1 à ${room.capacite}).`);
 const edit=state.modal?.id;
 if(currentBookings().some(b=>b.salle===room.nom&&b.id!==edit&&overlaps(b.dateDebut,b.dateFin,d1,d2)))throw Error('Conflit : cette salle est déjà réservée pour cette période.');
 const data={employeId:edit?(state.bookings.find(b=>b.id===edit)?.employeId||state.user.id):state.user.id,salleId,dateDebut:d1,dateFin:d2,motif,nombreParticipants:parts};
 if(state.backend){
  const r=await fetch(edit?`/api/v1/reservations/${edit}`:'/api/v1/reservations',{method:edit?'PUT':'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify(data)});
  if(!r.ok){const e=await r.json().catch(()=>({}));throw Error(e.erreur||`Erreur du serveur HTTP ${r.status}`)}
  await loadData();
 }else{
  const b={id:edit||Date.now(),salle:room.nom,salleId,employe:state.user.nom,employeId:state.user.id,dateDebut:d1,dateFin:d2,motif,participants:parts,statut:'CONFIRMEE'};
  if(edit){state.bookings=state.bookings.map(old=>old.id===edit?b:old)}else state.bookings.unshift(b);
  persistDemo();
 }
 state.modal=null;state.view='reservations';showToast(edit?'Réservation modifiée avec succès.':'Réservation créée et confirmée.');
}
async function cancelBooking(id){if(!confirm('Annuler cette réservation ?'))return;
 try{
  if(state.backend){const r=await fetch(`/api/v1/reservations/${id}`,{method:'DELETE'});if(!r.ok)throw Error(`Erreur du serveur : ${r.status}`);await loadData()}
  else {state.bookings=state.bookings.map(b=>b.id===id?{...b,statut:'ANNULEE'}:b);persistDemo()}
  showToast('Réservation annulée.');
 }catch(e){showToast(eMsg(e),true)}
}
function navMarkup(){const nav=[['dashboard','Vue d’ensemble','grid'],['calendar','Calendrier','calendar'],['rooms','Salles','door'],['reservations','Réservations','clock'],['statistics','Statistiques','bar']];
 return `<aside class="sidebar ${state.menu?'open':''}"><div class="brand"><span class="brand-mark">${ico('calendar',21)}</span><span>Salleo</span></div>
 <nav><p>ESPACE DE TRAVAIL</p>${nav.map(([key,label,icon])=>`<button data-view="${key}" class="${state.view===key?'active':''}">${ico(icon)} ${label}</button>`).join('')}</nav>
 <div class="sidebar-help"><span>${ico(state.backend?'shield':'info',18)}</span><strong>${state.backend?'Connexion à Spring Boot':'Mode démonstration'}</strong>
 <p>${state.backend?'Réservations enregistrées dans la base configurée sur le backend.':'Données sauvegardées localement dans ce navigateur.'}</p>
 <button type="button" data-view="guide">En savoir plus ${ico('arrow',12)}</button></div>
 <button class="logout" data-action="logout">${ico('logout',18)} Se déconnecter</button></aside>`;}
function topbar(){return `<header class="topbar"><button type="button" class="mobile-menu" data-action="menu" aria-label="Afficher le menu">${ico('menu',21)}</button>
 <div class="top-search">${ico('search',18)}<input id="searchInput" type="search" placeholder="Rechercher une salle ou une réunion…" value="${esc(state.search)}" aria-label="Rechercher"><kbd>Ctrl K</kbd></div>
 <div class="top-actions"><button class="icon-button notification" data-view="reservations" aria-label="Consulter les réservations">${ico('bell',20)}<i></i></button><span class="divider"></span><div class="avatar">${esc(firstLastName(state.user.nom))}</div>
 <div class="profile-name"><strong>${esc(state.user.nom)}</strong><span>${esc(state.user.role)}</span></div>${ico('down',16)}</div></header>`;}
function loginPage(){return `<main class="login-page"><section class="login-brand-panel"><div class="brand brand-light"><span class="brand-mark">${ico('calendar',23)}</span><span>Salleo</span></div>
 <div class="login-pitch"><span class="eyebrow">${ico('sparkles',14)} Espace de travail intelligent</span><h1>Vos réunions,<br>simplement organisées.</h1>
 <p>Centralisez les disponibilités, réservez la bonne salle et gardez une équipe parfaitement synchronisée.</p>
 <div class="feature-row"><div>${ico('check',17)}</div><span>Disponibilités consultables à tout moment</span></div>
 <div class="feature-row"><div>${ico('check',17)}</div><span>Aucun conflit de réservation</span></div>
 <div class="feature-row"><div>${ico('check',17)}</div><span>Statistiques d’utilisation simplifiées</span></div></div>
 <p class="copyright">© ${new Date().getFullYear()} Salleo · Gestion des espaces</p></section>
 <section class="login-form-panel"><div class="mobile-brand brand"><span class="brand-mark">${ico('calendar',21)}</span><span>Salleo</span></div>
 <form id="loginForm" class="login-card"><span class="login-icon">${ico('shield',24)}</span><h2>Heureux de vous revoir</h2>
 <p>Connectez-vous pour accéder à votre espace.</p><label>Adresse e-mail<input type="email" name="email" value="sami@example.com" autocomplete="username" required></label>
 <label>Mot de passe<div class="password-field"><input type="password" name="password" value="demo123" autocomplete="current-password" required><button type="button" data-action="toggle-password">Afficher</button></div></label>
 <div class="form-options"><label class="remember"><input type="checkbox" checked> Se souvenir de moi</label><button type="button" class="text-button" data-action="fill-manager">Compte responsable</button></div>
 <button class="primary-button login-button" type="submit">Se connecter ${ico('arrow',18)}</button>
 ${state.loginError?`<div class="login-warning" role="alert">${esc(state.loginError)}</div>`:''}
 <p class="demo-note">Démo d'interface : sami@example.com / demo123<br>Responsable : responsable@example.com / demo123</p>
 <p class="site-banner">La connexion est simulée côté navigateur. Une authentification serveur est nécessaire avant toute mise en production.</p></form></section></main>`;}
function statCard(icon,title,value,desc,color){return `<article class="stat-card ${color}-stat"><span class="stat-icon">${ico(icon,20)}</span><div><p>${title}</p><strong>${value}</strong><small>${desc}</small></div></article>`;}
function roomColor(i){return ['coral','mint','blue'][i%3]}
function roomCard(room,i){const ok=availableNow(room);return `<article class="room-card" data-searchable="${esc((room.nom+' '+room.localisation).toLowerCase())}">
 <div class="room-visual ${roomColor(i)}">${ico('door',32)}<span class="${ok?'available':'busy'}"><i></i>${ok?'Disponible':'Occupée / indisponible'}</span></div>
 <div class="room-info"><div><h3>Salle ${esc(room.nom)}</h3><p>${esc(room.localisation)}</p></div><span>${ico('users',16)} ${room.capacite}</span></div>
 <button data-book="${room.id}" ${room.statut==='DISPONIBLE'?'':'disabled'}>${room.statut==='DISPONIBLE'?'Réserver cette salle':'Indisponible'}</button></article>`;}
function upcomingList(){let now=new Date();const list=currentBookings().filter(r=>new Date(r.dateFin)>=now).sort((a,b)=>new Date(a.dateDebut)-new Date(b.dateDebut)).slice(0,4);
 if(!list.length)return '<p class="empty-text">Aucune réservation à venir. Créez votre première demande !</p>';
 return `<div class="timeline">${list.map((r,i)=>`<div class="time">${fmtTime(r.dateDebut)}</div><article class="meeting meeting-${roomColor(i)}"><span class="meeting-accent"></span>
 <div><h3>${esc(r.motif)}</h3><p>${ico('building',15)} Salle ${esc(r.salle)} · ${esc(fmtDate(r.dateDebut))}</p></div>
 <div class="attendees"><span>${esc(firstLastName(r.employe))}</span><span>+${Math.max(0,r.participants-1)}</span></div>
 <span class="status confirmed">Confirmée</span></article>`).join('')}</div>`;
}
function dashboard(){const now=new Date();const todayCount=currentBookings().filter(b=>sameDay(b.dateDebut,now)).length;
 const avail=availableCount(),occupiedRooms=state.rooms.length-avail;
 return `<div class="content"><section class="welcome"><div><span class="date-label">${esc(fmtDate(now,{weekday:'long',day:'numeric',month:'long'}).toUpperCase())}</span>
 <h1>Bonjour ${esc(state.user.nom.split(' ')[0])},</h1><p>Voici ce qui se passe dans vos espaces aujourd’hui.</p></div>
 <button type="button" class="primary-button" data-action="new">${ico('plus',19)} Nouvelle réservation</button></section>
 <section class="stats-grid">${statCard('calendar',"Réservations aujourd'hui",todayCount,'Réservations confirmées','coral')}
 ${statCard('door','Salles disponibles',`${avail} <i>/ ${state.rooms.length}</i>`,`${occupiedRooms} salle(s) occupée(s) ou indisponible(s)`,'blue')}
 ${statCard('clock',"Taux d’occupation",`${occupancy()}%`,'Occupation des salles à cet instant','yellow')}</section>
 <div class="dashboard-grid"><section class="panel schedule-panel"><div class="panel-title"><div><span class="section-kicker">À VENIR</span><h2>Prochaines réservations</h2></div>
 <button data-view="calendar">Voir le calendrier ${ico('arrow',16)}</button></div>${upcomingList()}</section>
 <aside class="panel occupancy-panel"><div class="panel-title"><div><span class="section-kicker">EN DIRECT</span><h2>Occupation</h2></div>
 <span class="live"><i></i> Actualisé</span></div>
 <div class="donut-wrap"><div class="donut" style="background:conic-gradient(#2563eb 0 ${occupancy()}%, #d7dee8 ${occupancy()}% 100%)"><div><strong>${occupancy()}%</strong><span>occupées</span></div></div>
 <div class="legend"><span><i class="legend-coral"></i> Occupées <b>${occupiedRooms}</b></span><span><i class="legend-mint"></i> Libres <b>${avail}</b></span></div></div>
 <div class="peak">${ico('bar',19)}<div><strong>Disponibilité en temps réel</strong><span>Selon les créneaux confirmés et l'état des salles</span></div></div></aside></div>
 <section class="rooms-section"><div class="panel-title"><div><span class="section-kicker">RÉSERVATION RAPIDE</span><h2>Salles de réunion</h2></div>
 <button data-view="rooms">Voir toutes les salles ${ico('arrow',16)}</button></div>
 <div class="room-grid">${state.rooms.slice(0,3).map(roomCard).join('')}</div></section></div>`;}
function roomPage(){return `<div class="content"><div class="grid-title"><div><span class="section-kicker">ESPACES DE TRAVAIL</span><h1>Nos salles</h1>
 <p>Consultez les capacités et réservez votre créneau en quelques clics.</p></div><button class="primary-button" data-action="new">${ico('plus')} Réserver</button></div>
 <div class="filter-row"><span class="soft-tag">${ico('door',14)} ${state.rooms.length} salles au total</span>
 <span class="soft-tag">${ico('check',14)} ${availableCount()} disponible(s) maintenant</span></div>
 <div class="room-grid">${state.rooms.map(roomCard).join('')}</div>
 ${isResponsible()?'<p class="site-banner">Pour ajouter, désactiver ou modifier une salle : <a href="/salles" style="color:#2563eb;text-decoration:underline">Gestion des salles (Spring Boot)</a>.</p>':''}</div>`;}
function bookingsPage(){const bookings=[...state.bookings].sort((a,b)=>new Date(b.dateDebut)-new Date(a.dateDebut));return `<div class="content"><div class="grid-title"><div><span class="section-kicker">VOS DEMANDES</span><h1>Réservations</h1>
 <p>Suivez vos réservations, modifiez les horaires ou annulez une demande.</p></div><button class="primary-button" data-action="new">${ico('plus')} Nouvelle réservation</button></div>
 <section class="panel"><div class="panel-heading-row"><h2 style="font-size:16px">Toutes les réservations</h2><span class="soft-tag">${bookings.length} réservation(s)</span></div>
 <div class="table-scroll"><table class="data-table"><thead><tr><th>Salle / objet</th><th>Employé</th><th>Début</th><th>Fin</th><th>Participants</th><th>Statut</th><th>Actions</th></tr></thead>
 <tbody>${bookings.map(b=>`<tr data-searchable="${esc((b.salle+' '+b.motif+' '+b.employe).toLowerCase())}"><td><strong>Salle ${esc(b.salle)}</strong><div class="text-hint">${esc(b.motif)}</div></td>
 <td>${esc(b.employe)}</td><td>${esc(fmtDate(b.dateDebut))}<br>${esc(fmtTime(b.dateDebut))}</td><td>${esc(fmtDate(b.dateFin))}<br>${esc(fmtTime(b.dateFin))}</td>
 <td>${b.participants}</td><td><span class="status ${b.statut==='ANNULEE'?'pending':'confirmed'}">${b.statut==='ANNULEE'?'Annulée':'Confirmée'}</span></td>
 <td>${b.statut==='ANNULEE'?'—':`<div style="display:flex;gap:6px"><button class="small-action" data-edit="${b.id}" title="Modifier">${ico('edit',14)}</button><button class="small-action danger" data-cancel="${b.id}" title="Annuler">${ico('trash',14)}</button></div>`}</td></tr>`).join('')}
 ${!bookings.length?'<tr><td colspan="7">Aucune réservation.</td></tr>':''}</tbody></table></div></section></div>`;}
function calendarPage(){const d=new Date(state.calMonth.getFullYear(),state.calMonth.getMonth(),1),y=d.getFullYear(),m=d.getMonth();
 const first=(d.getDay()+6)%7,last=new Date(y,m+1,0).getDate();let cells=[];
 for(let k=0;k<42;k++){const day=k-first+1;const cellDate=new Date(y,m,day);const thisMonth=day>=1&&day<=last;
 const events=currentBookings().filter(b=>sameDay(b.dateDebut,cellDate));
 cells.push(`<div class="day-cell ${thisMonth?'':'dim'} ${sameDay(cellDate,new Date())?'today':''}"><strong>${cellDate.getDate()}</strong>${events.slice(0,3).map(b=>`<div class="calendar-item" title="${esc(b.motif)}">${esc(fmtTime(b.dateDebut))} · ${esc(b.salle)}</div>`).join('')}${events.length>3?`<div class="calendar-item">+${events.length-3} autres</div>`:''}</div>`);}
 return `<div class="content"><div class="grid-title"><div><span class="section-kicker">PLANIFICATION</span><h1>Calendrier</h1><p>Les réservations confirmées sur le mois sélectionné.</p></div>
 <button class="primary-button" data-action="new">${ico('plus')} Nouvelle réservation</button></div>
 <section class="panel"><div class="panel-heading-row"><div style="display:flex;align-items:center;gap:12px"><button class="small-action" data-month="-1" aria-label="Mois précédent">${ico('left',18)}</button>
 <h2 style="min-width:155px;text-align:center;font-size:18px;text-transform:capitalize">${esc(fmtDate(d,{month:'long',year:'numeric'}))}</h2>
 <button class="small-action" data-month="1" aria-label="Mois suivant">${ico('right',18)}</button></div><span class="soft-tag">${ico('calendar',14)} Vue mensuelle</span></div>
 <div class="calendar-board">${['Lun','Mar','Mer','Jeu','Ven','Sam','Dim'].map(s=>`<div class="calendar-head">${s}</div>`).join('')}${cells.join('')}</div></section></div>`;}
function statsPage(){const now=new Date();const start=new Date(now.getFullYear(),now.getMonth(),1),end=new Date(now.getFullYear(),now.getMonth()+1,1);const ms=end-start;
 const rows=state.rooms.map(r=>{const all=currentBookings().filter(b=>b.salle===r.nom);let minutes=0;
  for(const b of all){const a=Math.max(start.getTime(),new Date(b.dateDebut).getTime()),z=Math.min(end.getTime(),new Date(b.dateFin).getTime());if(z>a)minutes+=(z-a)/60000;}
  return {room:r, count:all.length,percent:Math.min(100,Math.round(minutes*60000/ms*100*100)/100)};
 });
 return `<div class="content"><div class="grid-title"><div><span class="section-kicker">RAPPORT D'UTILISATION</span><h1>Statistiques</h1><p>Vue synthétique calculée depuis les réservations confirmées.</p></div><span class="soft-tag">${ico('calendar',14)} ${esc(fmtDate(now,{month:'long',year:'numeric'}))}</span></div>
 <section class="stats-grid">${statCard('calendar','Réservations confirmées',currentBookings().length,'Enregistrées dans le système','coral')}
 ${statCard('door','Salles gérées',state.rooms.length,'Toutes les salles','blue')}
 ${statCard('bar','Salles utilisées',new Set(currentBookings().map(b=>b.salle)).size,'Ayant au moins une réservation','yellow')}</section>
 <section class="panel"><div class="panel-heading-row"><h2 style="font-size:17px">Occupation par salle</h2><span class="soft-tag">Mois en cours (24 h/24)</span></div>
 <div class="occupancy-list">${rows.map(v=>`<div class="occupancy-row"><div><strong>${esc(v.room.nom)}</strong><div class="bar" style="margin-top:7px"><span style="width:${v.percent}%"></span></div></div><span>${v.percent.toLocaleString('fr-FR')} %</span></div>`).join('')}</div>
 <p class="text-hint" style="margin-top:20px">Taux calculé sur l'ensemble du mois (24 heures/jour). Pour un rapport détaillé : <a style="color:#2563eb;text-decoration:underline" href="/statistiques">Statistiques Spring Boot</a>.</p></section></div>`;}
function guidePage(){return `<div class="content"><div class="grid-title"><div><span class="section-kicker">AIDE ET INFORMATION</span><h1>Guide Salleo</h1><p>Comment utiliser l'application de réservation.</p></div></div>
 <section class="panel"><h2 style="font-size:18px;margin-bottom:14px">Le parcours en 4 étapes</h2>
 <div class="occupancy-list" style="font-size:13px;color:#475569"><p>1. Ouvrez <strong>Salles</strong> pour consulter les espaces et leur capacité.</p><p>2. Cliquez sur <strong>Réserver</strong>, indiquez une salle, les horaires et le nombre de participants.</p>
 <p>3. Cliquez sur <strong>Confirmer</strong> : une règle métier empêche le chevauchement et les dépassements de capacité.</p>
 <p>4. Retrouvez votre réservation dans le <strong>Calendrier</strong> ou dans <strong>Réservations</strong> pour la modifier ou l'annuler.</p></div>
 <div class="site-banner" style="margin-top:18px">La page de connexion fournie est une démonstration d'interface (mot de passe côté client). Ce n'est pas une protection d'accès aux API ni une authentification de production.</div></section></div>`;}
function bookingModal(){let found=state.modal?.id?state.bookings.find(b=>b.id===state.modal.id):null;
 const d=new Date();d.setDate(d.getDate()+1);d.setHours(11,0,0,0);let start=found?.dateDebut||isoLocal(d);d.setHours(12,0,0,0);let end=found?.dateFin||isoLocal(d);
 const id=found?.salleId||state.modal?.roomId||state.rooms[0]?.id;
 return `<div class="modal-backdrop" id="backdrop"><form id="bookingForm" class="modal"><div class="modal-heading"><div><span class="section-kicker">${found?'MODIFICATION':'NOUVELLE DEMANDE'}</span><h2>${found?'Modifier la réservation':'Réserver une salle'}</h2></div>
 <button class="icon-button" type="button" data-action="close" aria-label="Fermer">${ico('x',20)}</button></div>
 <div class="form-grid"><label class="full">Objet de la réunion<input name="motif" maxlength="300" required placeholder="Ex. Point équipe hebdomadaire" value="${esc(found?.motif||'')}"></label>
 <label>Salle<select name="salleId" required>${state.rooms.map(s=>`<option value="${s.id}" ${s.id===id?'selected':''}>Salle ${esc(s.nom)} · ${s.capacite} places</option>`).join('')}</select></label>
 <label>Participants<input name="nombreParticipants" type="number" min="1" max="100" required value="${found?.participants||6}"></label>
 <label>Début<input type="datetime-local" name="dateDebut" required value="${esc(start)}"></label>
 <label>Fin<input type="datetime-local" name="dateFin" required value="${esc(end)}"></label>
 <p class="site-banner full">Le système vérifiera la capacité de la salle et les conflits avant de confirmer.</p></div>
 <div id="modalError" role="alert"></div><div class="modal-actions"><button class="secondary-button" type="button" data-action="close">Annuler</button>
 <button class="primary-button" type="submit">${ico('check',17)} ${found?'Enregistrer les modifications':'Confirmer la réservation'}</button></div></form></div>`;}
function render(){if(!state.user){root.innerHTML=loginPage();return}
 const content={dashboard:dashboard,rooms:roomPage,reservations:bookingsPage,calendar:calendarPage,statistics:statsPage,guide:guidePage};
 root.innerHTML=`<div class="app-shell">${navMarkup()}<main class="dashboard">${topbar()}${(content[state.view]||dashboard)()}</main></div>
 ${state.modal?bookingModal():''}${state.toast?`<div class="toast ${state.toast.error?'error':''}" role="status"><span>${ico(state.toast.error?'info':'check',17)}</span>${esc(state.toast.text)}</div>`:''}`;
 if(state.search)filterSearch();}
function filterSearch(){const q=state.search.toLowerCase().trim();for(const el of document.querySelectorAll('[data-searchable]')){el.style.display=(el.getAttribute('data-searchable')||'').includes(q)?'':'none'}}
root.addEventListener('click',async e=>{
 const view=e.target.closest('[data-view]');if(view){state.view=view.dataset.view;state.menu=false;state.search='';render();return}
 const action=e.target.closest('[data-action]');if(action){switch(action.dataset.action){
 case 'logout':state.user=null;try{sessionStorage.removeItem('salleo.user')}catch{};state.view='dashboard';state.modal=null;render();break;
 case 'new':state.modal={};render();break;
 case 'close':state.modal=null;render();break;
 case 'menu':state.menu=!state.menu;render();break;
 case 'fill-manager':$('#loginForm input[name=email]').value='responsable@example.com';$('#loginForm input[name=password]').value='demo123';break;
 case 'toggle-password':{const inp=$('#loginForm input[name=password]');const shown=inp.type==='password';inp.type=shown?'text':'password';action.textContent=shown?'Masquer':'Afficher';break}
 }return;}
 const month=e.target.closest('[data-month]');if(month){state.calMonth=new Date(state.calMonth.getFullYear(),state.calMonth.getMonth()+Number(month.dataset.month),1);render();return}
 const book=e.target.closest('[data-book]');if(book){state.modal={roomId:Number(book.dataset.book)};render();return}
 const edit=e.target.closest('[data-edit]');if(edit){state.modal={id:Number(edit.dataset.edit)};render();return}
 const cancel=e.target.closest('[data-cancel]');if(cancel){await cancelBooking(Number(cancel.dataset.cancel));return}
 if(e.target.id==='backdrop'){state.modal=null;render()}
});
root.addEventListener('input',e=>{if(e.target.id==='searchInput'){state.search=e.target.value;filterSearch()}});
root.addEventListener('submit',async e=>{e.preventDefault();const form=e.target;
 if(form.id==='loginForm'){
  const fd=new FormData(form),email=String(fd.get('email')||'').trim().toLowerCase(),pw=String(fd.get('password')||'');
  if(pw==='demo123'&&users[email]){state.user={...users[email],email};try{sessionStorage.setItem('salleo.user',JSON.stringify(state.user))}catch{};state.loginError='';render();}
  else{state.loginError='Identifiants de démonstration incorrects.';render();}
 }
 if(form.id==='bookingForm'){
  const btn=form.querySelector('button[type=submit]');btn.disabled=true;try{await saveBooking(form)}catch(err){const box=$('#modalError');if(box){box.className='form-warning';box.textContent=eMsg(err)}btn.disabled=false}
 }
});
document.addEventListener('keydown',e=>{if(e.key==='Escape'&&state.modal){state.modal=null;render()}if((e.ctrlKey||e.metaKey)&&e.key.toLowerCase()==='k'){e.preventDefault();$('#searchInput')?.focus()}});
loadData();

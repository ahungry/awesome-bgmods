console.log('js 1 loaded')

function dosearch()
{
  const filter = document.getElementById('modsearch').value
  const xs = document.querySelectorAll('ul li')
  for (let i = 1; i < xs.length; i++) {
    if (xs[i].innerHTML.toLowerCase().indexOf(filter) > -1)
      xs[i].style.display='block'
    else
      xs[i].style.display='none'
  }
}

function snagtags(){
  const bar = document.querySelector('.bottom-search-bar')
  const tags = document.querySelectorAll('img')
  const m = {}
  for (let i = 0; i < tags.length; i++) {
    const match = tags[i].src.match(/.*(tag-.*?)-/)
    if (match) {
      const el = document.createElement('img')
      el.src = tags[i].src
      el.className = 'barbutton'
      el.onclick = () => { document.getElementById('modsearch').value = match[1] }
      m[match[1]] = el
    }
  }
  Object.keys(m).forEach(k => {
    bar.append(m[k])
  })
}

function appendsearch() {
  document.body.innerHTML+=`<div class="spacer">&nbsp;</div>
<div class="bottom-search-bar">tags:
<input type="text" id="modsearch" />
<br />
(ex: 'tag-audio' finds audio mods, 'drizzt' finds any mods that
mention him, 'cmp-ahungry001' finds mods that ahungry used in tandem, 'dist-g3'
finds mods from gibberlings3)<br />
</div>`
}

setInterval(dosearch, 100)

const links = document.querySelectorAll('a')
for (let i = 0; i < links.length; i++)
  if (links[i].href.indexOf('#') === -1)
    links[i].target='_blank'

window.onload=() => {
  appendsearch()
  snagtags()
}

console.log('js 2 loaded')

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
  const tagbar = document.querySelector('.bottom-search-bar .tags')
  const distbar = document.querySelector('.bottom-search-bar .dists')
  const cmpbar = document.querySelector('.bottom-search-bar .cmps')
  const naybar = document.querySelector('.bottom-search-bar .nays')
  const tags = document.querySelectorAll('img')
  const m = {}
  for (let i = 0; i < tags.length; i++) {
    const match = tags[i].src.match(/.*((tag|dist|cmp|nay)-.*?)-/)
    if (match) {
      const el = document.createElement('img')
      el.src = tags[i].src
      el.className = 'barbutton'
      el.onclick = () => { document.getElementById('modsearch').value = match[1] }
      m[match[1]] = el
    }
  }
  Object.keys(m).forEach(k => {
    console.log(k)
    if (/^tag/.test(k))
      tagbar.append(m[k])

    if (/^dist/.test(k))
      distbar.append(m[k])

    if (/^cmp/.test(k))
      cmpbar.append(m[k])

    if (/^nay/.test(k))
      naybar.append(m[k])
  })
}

function help() {
  alert(`ex: 'tag-audio' finds audio mods, 'drizzt' finds any mods that
mention him, 'cmp-ahungry001' finds mods that ahungry used in tandem, 'dist-g3'
finds mods from gibberlings3
`)
}

function appendsearch() {
  document.body.innerHTML+=`<div class="spacer">&nbsp;</div>
<div class="bottom-search-bar">
<input type="text" id="modsearch" />
<button class="help" onclick='help()'>help</button>
<h6>tags</h6>
<div class="tags overflower"></div>
<h6>dists</h6>
<div class="dists overflower"></div>
<h6>cmps</h6>
<div class="cmps overflower"></div>
<h6>nays</h6>
<div class="nays overflower"></div>
</div>
</div>
`
}

const links = document.querySelectorAll('a')
for (let i = 0; i < links.length; i++)
  if (links[i].href.indexOf('#') === -1)
    links[i].target='_blank'

window.onload=() => {
  appendsearch()
  snagtags()
  setInterval(dosearch, 100)
}

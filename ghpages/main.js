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
      const anchor = document.createElement('a')
      anchor.href = '#' + match[1]
      tags[i].parentElement.append(anchor)

      const el = document.createElement('img')
      el.src = tags[i].src
      el.className = 'barbutton'
      el.onclick = () => {
        document.getElementById('modsearch').value = match[1]
        window.scrollTo(0, 0)
        setTimeout(
          () => {
            document.querySelector('a[href="#' + match[1] + '"]')
              .scrollIntoView(true)
            setTimeout(() => { window.scrollBy(0, -200) }, 100)
          },
          100
        )
      }
      m[match[1]] = el
    }
  }
  Object.keys(m).forEach(k => {
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
function reset() {
  document.getElementById('modsearch').value = ''
}

function appendsearch() {
  document.body.innerHTML+=`<div class="spacer">&nbsp;</div>
<div class="bottom-search-bar">
<input type="text" id="modsearch" />
<button class="reset" onclick='reset()'>reset</button>
<button class="help" onclick='help()'>help</button>
<h5>tags</h5>
<div class="tags overflower"></div>
<h5>dists</h5>
<div class="dists overflower"></div>
<h5>cmps</h5>
<div class="cmps overflower"></div>
<h5>nays</h5>
<div class="nays overflower"></div>
</div>
</div>
`
}

const links = document.querySelectorAll('a')
for (let i = 0; i < links.length; i++)
  if (links[i].href.indexOf('#') === -1)
    links[i].target='_blank'

function searchToggle(){
  if (window.innerWidth < 800) {
    document.querySelector('.bottom-search-bar').style.display='block'
    return
  }
  if (window.scrollY > 600) {
    document.querySelector('.bottom-search-bar').style.display='block'
  } else {
    document.querySelector('.bottom-search-bar').style.display='none'
  }
}

window.onload=() => {
  appendsearch()
  snagtags()
  setInterval(dosearch, 100)
  // setInterval(searchToggle, 300)
}

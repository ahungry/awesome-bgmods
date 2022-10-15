console.log('js 2 loaded')

function dosearch()
{
  const filterv = document.getElementById('modsearch').value
  const filters = filterv.split(',')

    const xs = document.querySelectorAll('.mod-entry')
    for (let i = 0; i < xs.length; i++) {
      let hasMatch = true

      filters.forEach(filter => {
        if (xs[i].innerHTML.toLowerCase().indexOf(filter.trim()) === -1) {
          hasMatch = false
        }
      })

      if (hasMatch)
        xs[i].style.display='block'
      else
        xs[i].style.display='none'
    }
}

function snagtags(){
  const tagbar = document.querySelector('.bottom-search-bar .tags')
  const typebar = document.querySelector('.bottom-search-bar .types')
  const distbar = document.querySelector('.bottom-search-bar .dists')
  const tags = document.querySelectorAll('img')
  const m = {}
  for (let i = 0; i < tags.length; i++) {
    const match = tags[i].src.match(/.*((tag|type|dist)-.*?)-/)

    if (match) {
      const anchor = document.createElement('a')
      anchor.href = '#' + match[1]
      tags[i].parentElement.append(anchor)

      const el = document.createElement('img')
      el.src = tags[i].src
      el.className = 'barbutton'
      el.onclick = () => {
        if (el.className.indexOf('active') > -1) {
          el.className = 'barbutton'
        } else {
          el.className = 'barbutton active'
        }

        const els = document.querySelectorAll('.active')
        const modsearches = []
        for (let i = 0; i < els.length; i++) {
          const innermatch = els[i].src.match(/.*((tag|type|dist)-.*?)-/)
          modsearches.push(innermatch[1])
        }
        document.getElementById('modsearch').value = modsearches.join(',')

        window.history.replaceState({}, 'ignored', '#' + modsearches.join(','))
        // window.scrollTo(0, 0)
        // setTimeout(
        //   () => {
        //     document.querySelector('a[href="#' + match[1] + '"]')
        //       .scrollIntoView(true)
        //     setTimeout(() => { window.scrollBy(0, -200) }, 100)
        //   },
        //   100
        // )
      }
      m[match[1]] = el
    }
  }
  Object.keys(m).sort().forEach(k => {
    if (/^tag-.+/.test(k))
      tagbar.append(m[k])

    if (/^dist-.+/.test(k))
      distbar.append(m[k])
  })

  Object.keys(m).forEach(k => {
    if (/^type/.test(k))
      typebar.append(m[k])
  })
}

function help() {
  alert(`ex: 'tag-audio' finds audio mods, 'drizzt' finds any mods that
mention him, 'dist-g3' finds mods from gibberlings3
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
<h5>tags (AND condition/narrows results)</h5>
<div class="tags overflower"></div>
<h5>mod type for install order (1 max)</h5>
<div class="types overflower"></div>
<h5>mod distributor (1 max)</h5>
<div class="dists overflower"></div>
</div>
</div>
`
}

function linkify() {
  const links = document.querySelectorAll('a')
  for (let i = 0; i < links.length; i++)
    if (links[i].href.indexOf('#') === -1)
      links[i].target='_blank'
}

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

function resumeState () {
  const segments = window.location.href.match(/#(.*)/)[1] || false
  if (!segments) return
  const matches = segments.split(',')

  document.getElementById('modsearch').value = segments

  const els = document.querySelectorAll('.bottom-search-bar img')

  for (let i = 0; i < els.length; i++) {
    matches.forEach(m => {
      if (els[i].src.indexOf(m) > -1) {
        els[i].className += ' active'
      }
    })
  }
  document.getElementById('legend').scrollIntoView(true)
}

window.onload=() => {
  appendsearch()
  snagtags()
  linkify()
  resumeState()
  setInterval(dosearch, 100)
  // setInterval(searchToggle, 300)
}

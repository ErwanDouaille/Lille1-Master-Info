
// --------------------------------------------------------------------
// Master IVI, module M3DA modelisation 3D avancee
// F. Cabestaing (2010)
// --------------------------------------------------------------------
// TP1: Estimation de pose d'une camera

#declare Mire = union {
  box {
    <-dGridSize * (iCornersX + 1) / 2.0, 0, -dGridSize *
      (iCornersY + 1) / 2.0>
    <dGridSize * (iCornersX + 1) / 2.0,  -dGridSize / 10,
      dGridSize * (iCornersY + 1) / 2.0>
    pigment { checker color Black color White scale dGridSize }
  }
  box {
    <-dGridSize * (iCornersX + 2) / 2.0, -0.001, -dGridSize *
      (iCornersY + 2) / 2.0>
    <dGridSize * (iCornersX + 2) / 2.0,  -dGridSize / 10,
      dGridSize * (iCornersY + 2) / 2.0>
    pigment { White }
  }
}

